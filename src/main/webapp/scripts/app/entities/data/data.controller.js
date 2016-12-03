'use strict';

angular.module('piaApp')
        .controller('DataController', function ($scope, $state, Repo, Plugin, ParseLinks) {

            $scope.plugins = {};
            $scope.predicate = 'id';
            $scope.reverse = true;
            $scope.page = 0;
            $scope.pluginCount = 0;
            $scope.itemCount = 0;
            /*
             $scope.itemCount = function() {
             var count = 0;
             for (var plugin in $scope.plugins)
             {
             console.log($scope.plugins[plugin]);
             
             if ($scope.plugins.hasOwnProperty(plugin) && $scope.plugins[plugin].stat.count !== undefined)
             {
             count += $scope.plugins[plugin].stat.count;
             }
             };
             return count;
             };
             */
            $scope.loadAll = function () {

                Plugin.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    
                    if (result.length > 0) { // TODO: This is quite the ugly hack, but I don't know why loadAll() is called so often in the functions below. Maybe Mr. Fabianek had a reason to do so?
                        $scope.pluginCount = result.length;
                    }
                        
                    $scope.itemCount = 0;

                    result.forEach(function (plugin) {
                        $scope.plugins[plugin.id] = {"plugin": plugin, "stat": []};
                        Plugin.stats({id: plugin.id}, function (stat) {
                            if (stat) {
                                $scope.plugins[plugin.id]["stat"] = stat;
                                stat.forEach(function (repository) {
                                    $scope.itemCount += repository.count;
                                });
                            }

                        });
                    });
                });
            };

            $scope.reset = function () {
                $scope.page = 0;
                $scope.plugins = {};
                $scope.loadAll();
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.loadAll();

            $scope.refresh = function () {
                $scope.reset();
                $scope.clear();
            };

            $scope.clear = function () {
                $scope.repo = {
                    identifier: null,
                    description: null,
                    creator: null,
                    id: null,
                };
            };
        })