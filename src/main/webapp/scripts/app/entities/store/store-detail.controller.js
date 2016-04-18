'use strict';

angular.module('piaApp')
    .controller('StoreDetailController', function ($scope, $rootScope, $stateParams, entity, Store) {
        $scope.store = entity;
        $scope.load = function (id) {
            Store.get({id: id}, function(result) {
                $scope.store = result;
            });
        };
        var unsubscribe = $rootScope.$on('piaApp:storeUpdate', function(event, result) {
            $scope.store = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
