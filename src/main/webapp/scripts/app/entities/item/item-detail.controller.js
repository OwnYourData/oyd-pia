'use strict';

angular.module('piaApp')
    .controller('ItemDetailController', function ($scope, $rootScope, $stateParams, entity, Item, Repo) {
        $scope.item = entity;
        $scope.load = function (id) {
            Item.get({id: id}, function(result) {
                $scope.item = result;
            });
        };
        var unsubscribe = $rootScope.$on('piaApp:itemUpdate', function(event, result) {
            $scope.item = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
