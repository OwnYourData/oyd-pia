'use strict';

angular.module('piaApp')
    .controller('DatatypeDetailController', function ($scope, $rootScope, $stateParams, entity, Datatype) {
        $scope.datatype = entity;
        $scope.load = function (id) {
            Datatype.get({id: id}, function(result) {
                $scope.datatype = result;
            });
        };
        var unsubscribe = $rootScope.$on('piaApp:datatypeUpdate', function(event, result) {
            $scope.datatype = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
