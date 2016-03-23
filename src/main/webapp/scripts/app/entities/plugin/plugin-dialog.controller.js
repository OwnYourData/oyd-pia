'use strict';

angular.module('piaApp').controller('PluginDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Plugin',
        function($scope, $stateParams, $uibModalInstance, entity, Plugin) {

        $scope.plugin = entity;
        $scope.load = function(id) {
            Plugin.get({id : id}, function(result) {
                $scope.plugin = result;
            });
        };

        $scope.loadSecret = function(id) {
            Plugin.secret({id: id}, function(result) {
               $scope.plugin.secret = result.secret;
            });
        }

        var onSaveSuccess = function (result) {
            $scope.$emit('piaApp:pluginUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.plugin.id != null) {
                Plugin.update($scope.plugin, onSaveSuccess, onSaveError);
            } else {
                Plugin.save($scope.plugin, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
