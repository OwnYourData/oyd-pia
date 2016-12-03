'use strict';

angular.module('piaApp')
        .controller('AppController', function ($rootScope, $scope, Principal, $state, $timeout, $location, Auth, AuthServerProvider, Plugin, Store, ParseLinks, Upload, ngDialog) {

            // Taken from old Main controller

            Principal.identity().then(function (account) {
                $scope.account = account;
                $scope.isAuthenticated = Principal.isAuthenticated;
            });

            $scope.user = {};
            $scope.errors = {};
            $scope.qr = JSON.stringify({id: AuthServerProvider.getClientId(), secret: AuthServerProvider.getClientSecret(), protocol: location.protocol, host: location.host});

            $scope.rememberMe = true;
            $timeout(function () {
                angular.element('[ng-model="username"]').focus();
            });
            $scope.login = function (event) {
                event.preventDefault();
                Auth.login({
                    username: $scope.username,
                    password: $scope.password,
                    rememberMe: $scope.rememberMe
                }).then(function () {
                    $scope.loadAll();

                    $scope.authenticationError = false;
                    if ($rootScope.previousStateName === 'register') {
                        $state.go('app');
                    } else {
                        $rootScope.back();
                    }
                }).catch(function () {
                    $scope.authenticationError = true;
                });
            };


            // New stuff



            $scope.installedPlugins = [];
            $scope.stores = [];
            $scope.predicate = 'id';
            $scope.reverse = true;
            $scope.page = 0;
            $scope.description = false;

            $scope.loadAll = function () {
                // Reset old data
                $scope.installedPlugins = [];
                $scope.stores = [];

                Plugin.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    if (result.length === 0) {
                        $scope.description = true;
                    }

                    result.forEach(function (installedPlugin) {
                        $scope.installedPlugins.push(installedPlugin);
                    });
                });

                Store.query(function (result) {
                    result.forEach(function (store) {
                        $scope.stores.push(store);
                        $scope.loadPlugins(store);
                    });
                });
            };

            $scope.reset = function () {
                $scope.page = 0;
                $scope.loadAll();
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };


            $scope.refresh = function () {
                $scope.reset();
                $scope.clear();
            };

            $scope.clear = function () {
                $scope.plugin = {
                    name: null,
                    identifier: null,
                    path: null,
                    id: null,
                    items: null
                };
            };

            $scope.start = function (plugin) {
                Plugin.start(plugin);
                $state.go('app', null, {reload: true});
            };

            $scope.stop = function (plugin) {
                Plugin.stop(plugin);
                $state.go('app', null, {reload: true});
            };

            $scope.upload = function (file) {
                if (file) {
                    ngDialog.openConfirm({
                        template: 'scripts/app/entities/plugin/permissions.html',
                        showClose: false,
                        controller: 'PluginPermissionController',
                        data: file
                    }).then(function (success) {
                        Upload.upload({
                            url: 'api/plugins/upload',
                            data: {'file': file, 'name': 'plugin'}
                        }).then(function (resp) {
                            console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
                            $state.go('plugin', null, {reload: true});
                        }, function (resp) {
                            console.log('Error status: ' + resp.status);
                            console.log(resp);
                        }, function (evt) {
                            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                            console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                        });
                    }, function (error) {
                        console.log('Modal promise rejected. Reason: ', error);

                    });
                }
            };

            $scope.loadPlugins = function (store) {
                store.plugins = [];

                Store.plugins({id: store.id}, function (result) {
                    var existingIdentifiers = $scope.installedPlugins.map(function (a) {
                        return a.identifier;
                    });

                    for (var i = 0; i < result.length; i++) {
                        if (existingIdentifiers.indexOf(result[i].identifier) === -1)
                        {
                            store.plugins.push(result[i]);
                        }
                    }
                })
            };

            $scope.install = function (store, plugin) {
                Plugin.register(plugin, function () {
                    $state.go('app', null, {reload: true});
                });

            };

            $scope.loadAll();

        });