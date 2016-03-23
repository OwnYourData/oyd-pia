'use strict';

angular.module('piaApp')
    .controller('RepoController', function ($scope, $state, Repo, ParseLinks) {

        $scope.repos = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Repo.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.repos.push(result[i]);
                }
            });
            Repo.counts(function(result) {
                $scope.counts = result;
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.repos = [];
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
            $scope.repo = {
                identifier: null,
                description: null,
                creator: null,
                id: null
            };
        };
    });
