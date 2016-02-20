'use strict';

angular.module('piaApp')
    .controller('DataDetailController', function ($scope, $rootScope, $stateParams, entity, Data, Datatype) {
        $scope.data = entity;
        $scope.load = function (id) {
            Data.get({id: id}, function(result) {
                $scope.data = result;
            });
        };
        var unsubscribe = $rootScope.$on('piaApp:dataUpdate', function(event, result) {
            $scope.data = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
