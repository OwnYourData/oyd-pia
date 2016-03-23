'use strict';

angular.module('piaApp')
	.controller('RepoDeleteController', function($scope, $uibModalInstance, entity, Repo) {

        $scope.repo = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Repo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
