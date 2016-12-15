'use strict';

angular.module('piaApp')
        .controller('DataItemsController', function ($scope, $stateParams, Repo, ParseLinks) {

            $scope.repo = null;
            $scope.items = [];
            $scope.predicate = 'id';
            $scope.reverse = true;
            $scope.page = 0;
            $scope.loadInProgress = false;
            $scope.MAX_DESCRIPTION_LENGTH = 120;

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
                        var itemText = JSON.stringify(item);
                        item.itemText = itemText;
                        item.showAllLink = true;

                        if (itemText.length > $scope.MAX_DESCRIPTION_LENGTH) {
                            item.shortText = itemText.substring(0, $scope.MAX_DESCRIPTION_LENGTH);
                        } else {
                            item.shortText = null;
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

