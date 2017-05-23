'use strict';

angular.module('piaApp').controller('ItemDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Item', 'Repo',
        function($scope, $stateParams, $uibModalInstance, entity, Item, Repo) {

        $scope.item = entity;
        $scope.repos = Repo.query({
            page: 0,
            size: 100});
        $scope.load = function(id) {
            Item.get({id : id}, function(result) {
                $scope.item = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('piaApp:itemUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.item.id != null) {
                Item.update($scope.item, onSaveSuccess, onSaveError);
            } else {
                Item.save($scope.item, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
