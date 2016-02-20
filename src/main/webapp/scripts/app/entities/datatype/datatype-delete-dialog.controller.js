'use strict';

angular.module('piaApp')
	.controller('DatatypeDeleteController', function($scope, $uibModalInstance, entity, Datatype) {

        $scope.datatype = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Datatype.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
