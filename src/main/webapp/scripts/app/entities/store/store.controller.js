'use strict';

angular.module('piaApp')
    .controller('StoreController', function ($scope, $state, Store) {

        $scope.stores = [];
        $scope.loadAll = function() {
            Store.query(function(result) {
               $scope.stores = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.store = {
                name: null,
                url: null,
                id: null
            };
        };
    });
