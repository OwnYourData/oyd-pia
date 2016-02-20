'use strict';

angular.module('piaApp').controller('DatatypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Datatype',
        function($scope, $stateParams, $uibModalInstance, entity, Datatype) {

        $scope.datatype = entity;
        $scope.load = function(id) {
            Datatype.get({id : id}, function(result) {
                $scope.datatype = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('piaApp:datatypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.datatype.id != null) {
                Datatype.update($scope.datatype, onSaveSuccess, onSaveError);
            } else {
                Datatype.save($scope.datatype, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
