'use strict';

angular.module('piaApp')
    .factory('Datatype', function ($resource, DateUtils) {
        return $resource('api/datatypes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'counts': { method: 'GET', url: 'api/datatypes/counts',
                transformResponse: function(data) {
                    var result = {};
                    angular.fromJson(data).forEach(function(value) {
                        result[value.type] = value.count;
                    });
                    return result;
                }},
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
