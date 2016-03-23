'use strict';

angular.module('piaApp').controller('RepoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Repo',
        function($scope, $stateParams, $uibModalInstance, entity, Repo) {

        $scope.repo = entity;
        $scope.load = function(id) {
            Repo.get({id : id}, function(result) {
                $scope.repo = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('piaApp:repoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.repo.id != null) {
                Repo.update($scope.repo, onSaveSuccess, onSaveError);
            } else {
                Repo.save($scope.repo, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
