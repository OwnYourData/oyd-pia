'use strict';

angular.module('piaApp')
    .factory('AuthServerProvider', function loginService($http, localStorageService, Base64) {
        return {
            login: function(credentials) {
                var data = "username=" +  encodeURIComponent(credentials.username) + "&password="
                    + encodeURIComponent(credentials.password) + "&grant_type=password&scope=read%20write&" +
                    "client_secret="+this.getClientSecret()+"&client_id="+this.getClientId();
                return $http.post('oauth/token', data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "Accept": "application/json",
                        "Authorization": "Basic " + Base64.encode(this.getClientId() + ':' + this.getClientSecret())
                    }
                }).success(function (response) {
                    var expiredAt = new Date();
                    expiredAt.setSeconds(expiredAt.getSeconds() + response.expires_in);
                    response.expires_at = expiredAt.getTime();
                    localStorageService.set('token', response);
                    return response;
                });
            },
            logout: function() {
                // logout from the server
                $http.post('api/logout').then(function() {
                    localStorageService.clearAll();
                });
            },
            getToken: function () {
                return localStorageService.get('token');
            },
            hasValidToken: function () {
                var token = this.getToken();
                return token && token.expires_at && token.expires_at > new Date().getTime();
            },
            getClientSecret: function() {
                return 'mySecretOAuthSecret';
            },
            getClientId: function() {
                return 'piaapp';
            }

        };
    });
