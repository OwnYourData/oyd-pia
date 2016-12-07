'use strict';

angular.module('piaApp')
        .controller('PasswordController', function ($scope, Auth, Principal, AuthServerProvider) {
            Principal.identity().then(function (account) {
                $scope.account = account;
            });

            $scope.success = null;
            $scope.error = null;
            $scope.doNotMatch = null;
            $scope.clientHost = location.host;
            $scope.clientSecret = AuthServerProvider.getClientSecret();
            $scope.clientId = AuthServerProvider.getClientId();
            $scope.qr = JSON.stringify({id: AuthServerProvider.getClientId(), secret: AuthServerProvider.getClientSecret(), protocol: location.protocol, host: location.host});
            $scope.changePassword = function () {
                if ($scope.password !== $scope.confirmPassword) {
                    $scope.error = null;
                    $scope.success = null;
                    $scope.doNotMatch = 'ERROR';
                } else {
                    $scope.doNotMatch = null;
                    Auth.changePassword($scope.password).then(function () {
                        $scope.error = null;
                        $scope.success = 'OK';
                    }).catch(function () {
                        $scope.success = null;
                        $scope.error = 'ERROR';
                    });
                }
            };
        });
