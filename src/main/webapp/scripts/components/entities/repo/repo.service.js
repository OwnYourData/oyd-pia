'use strict';

angular.module('piaApp')
    .factory('Repo', function ($resource, DateUtils) {
        return $resource('api/repos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'counts': { method: 'GET', url: 'api/repos/counts',
                transformResponse: function(data) {
                    var result = {};
                    angular.fromJson(data).forEach(function(value) {
                        result[value.type] = value.count;
                    });
                    return result;
                }},
            'update': { method:'PUT' }
        });
    });
