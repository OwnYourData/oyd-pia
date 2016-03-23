'use strict';

angular.module('piaApp')
    .controller('RepoDetailController', function ($scope, $rootScope, $stateParams, entity, Repo) {
        $scope.repo = entity;
        $scope.load = function (id) {
            Repo.get({id: id}, function(result) {
                $scope.repo = result;
            });
        };
        var unsubscribe = $rootScope.$on('piaApp:repoUpdate', function(event, result) {
            $scope.repo = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
