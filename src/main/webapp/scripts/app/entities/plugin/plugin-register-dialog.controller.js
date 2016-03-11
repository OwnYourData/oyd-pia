'use strict';

angular.module('piaApp')
    .controller('PluginRegisterDialogController', function($scope, $uibModalInstance,Plugin) {


        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.evaluate = function() {
            $scope.application = null;
            var decoded = atob($scope.base64);
            if (decoded) {
                $scope.application = JSON.parse(decoded);
            }

        };

        $scope.grant = function() {
            Plugin.register($scope.base64, function(result) {
                if (result) {
                    var clientDetails = {};
                    clientDetails.client_id = result.client_id;
                    clientDetails.client_secret = result.client_secret;

                    $scope.clientDetails = clientDetails;
                }
            });
        }

    });

