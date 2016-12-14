'use strict';

angular.module('piaApp')
        .controller('DataItemsController', function ($scope, $stateParams, Repo, ParseLinks) {

            $scope.repo = null;
            $scope.items = [];
            $scope.predicate = 'id';
            $scope.reverse = true;
            $scope.page = 0;
            $scope.loadInProgress = false;

            $scope.loadAll = function ($page) {
                $scope.loadInProgress = true;
                if ($scope.repo === null)
                {
                    Repo.get({id: $stateParams.repositoryId}, function (result) {
                        $scope.repo = result;
                        $scope.loadItems($page);
                    });
                } else {
                    $scope.loadItems($page);
                }

            }

            $scope.loadItems = function ($page) {
                Repo.items({identifier: $scope.repo.identifier, page: $page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
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
                    });
                    $scope.loadInProgress = false;

                });
            }


            $scope.reset = function () {
                $scope.page = 0;
                $scope.items = [];
                $scope.loadAll(0);
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll(page);
            };
            $scope.loadAll(0);



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

