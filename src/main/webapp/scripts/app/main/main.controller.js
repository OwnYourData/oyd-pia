'use strict';

angular.module('piaApp')
    .controller('MainController', function ($rootScope, $scope, Principal,$state, $timeout,$location, Auth,AuthServerProvider) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.user = {};
        $scope.errors = {};
        $scope.qr = JSON.stringify({id: AuthServerProvider.getClientId(), secret: AuthServerProvider.getClientSecret(), host: location.host});

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
