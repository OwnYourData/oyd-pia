'use strict';

angular.module('piaApp').controller('DataDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Data', 'Datatype',
        function($scope, $stateParams, $uibModalInstance, entity, Data, Datatype) {

        $scope.data = entity;
        $scope.datatypes = Datatype.query();
        $scope.load = function(id) {
            Data.get({id : id}, function(result) {
                $scope.data = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('piaApp:dataUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.data.id != null) {
                Data.update($scope.data, onSaveSuccess, onSaveError);
            } else {
                Data.save($scope.data, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
