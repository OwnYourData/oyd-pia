'use strict';

angular.module('piaApp')
    .controller('PluginController', function ($scope, $state, Plugin, ParseLinks, Upload, ngDialog) {

        $scope.plugins = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Plugin.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.plugins.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.plugins = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.plugin = {
                name: null,
                identifier: null,
                path: null,
                id: null
            };
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
                        $state.go('plugin', null, { reload: true });
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
    });
