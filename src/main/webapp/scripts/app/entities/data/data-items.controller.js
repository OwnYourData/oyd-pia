'use strict';

angular.module('piaApp')
        .controller('DataItemsController', function ($scope, $stateParams, Repo) {

            $scope.repo = null;
            $scope.items = [];

            $scope.load = function (id) {
                Repo.get({id: id}, function (result) {
                    $scope.repo = result;

                    Repo.items({identifier: $scope.repo.identifier}, function (result) {
                        result.forEach(function (item) {
                            $scope.items.push(item);
                            if (item.value !== null && item.value !== "undefined") {
                                var valueLength = JSON.stringify(item.value);
                                var maxLength = 120;
                                if (valueLength.length > maxLength) {
                                    item.value = valueLength.substring(0, maxLength);

                                    $scope.morebtn = {
                                        "display": "inline"
                                    }

                                } else {
                                    item.value = valueLength;
                                }

                                $scope.readMore = function () {
                                    item.value = valueLength;
                                }
                            }
                        })
                    });
                });
            };

            $scope.load($stateParams.repositoryId);

        });

