'use strict';

angular.module('piaApp')
	.controller('DataDeleteController', function($scope, $uibModalInstance, entity, Data) {

        $scope.data = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Data.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
