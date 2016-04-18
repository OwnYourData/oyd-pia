'use strict';

angular.module('piaApp').controller('StoreDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Store',
        function($scope, $stateParams, $uibModalInstance, entity, Store) {

        $scope.store = entity;
        $scope.load = function(id) {
            Store.get({id : id}, function(result) {
                $scope.store = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('piaApp:storeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.store.id != null) {
                Store.update($scope.store, onSaveSuccess, onSaveError);
            } else {
                Store.save($scope.store, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
