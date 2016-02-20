'use strict';

angular.module('piaApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


