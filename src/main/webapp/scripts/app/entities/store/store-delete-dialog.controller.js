'use strict';

angular.module('piaApp')
	.controller('StoreDeleteController', function($scope, $uibModalInstance, entity, Store) {

        $scope.store = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Store.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
