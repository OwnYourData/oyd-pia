'use strict';

angular.module('piaApp')
    .controller('DatatypeController', function ($scope, $state, Datatype) {

        $scope.datatypes = [];
        $scope.loadAll = function() {
            Datatype.query(function(result) {
               $scope.datatypes = result;
            });
            Datatype.counts(function(result) {
                $scope.counts = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.datatype = {
                name: null,
                description: null,
                id: null
            };
        };

        $scope.getCount = function(type) {
            console.log('get count of type '+type);
            return $scope.counts
        }
    });
