'use strict';

angular.module('piaApp')
    .factory('Store', function ($resource, DateUtils) {
        return $resource('api/stores/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'plugins': {method: 'GET', isArray: true, url: 'api/stores/:id/plugins'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
