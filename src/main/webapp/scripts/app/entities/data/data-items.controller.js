'use strict';

angular.module('piaApp')
        .controller('DataItemsController', function ($scope, $stateParams, Repo) {

            $scope.repo = null;
            $scope.items = [];
            $scope.page = 0;

            $scope.load = function (id) {
                Repo.get({id: id}, function (result) {
                    $scope.repo = result;

                    Repo.items({identifier: $scope.repo.identifier}, function (result) {
                        result.forEach(function (item) {
                            $scope.items.push(item);
                            if (item !== null && item !== "undefined") {
                                var valueLength = JSON.stringify(item);
                                var maxLength = 120;
                                if (valueLength.length > maxLength) {
                                    item = valueLength.substring(0, maxLength);

                                    $scope.morebtn = {
                                        "display": "inline"
                                    }

                                } else {
                                    item = valueLength;
                                }

                                $scope.readMore = function () {
                                    item = valueLength;
                                }
                            }
                        })
                    });
                });
            };
            $scope.reset = function () {
                $scope.page = 0;
                $scope.items = [];
                $scope.load($stateParams.repositoryId);
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.load($stateParams.repositoryId);
            };
            $scope.load($stateParams.repositoryId);
            $scope.refresh = function () {
                $scope.reset();
                $scope.clear();
            };
            $scope.clear = function () {
                $scope.item = {
                    value: null,
                    id: null
                };
            };
        });

