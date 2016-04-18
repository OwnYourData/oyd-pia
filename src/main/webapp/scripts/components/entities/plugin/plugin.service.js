'use strict';

angular.module('piaApp')
    .factory('Plugin', function ($resource, DateUtils) {
        return $resource('api/plugins/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'register': {method:'POST', url:'api/plugins/register'},
            'start' : { method:'GET', url:'api/plugins/:id/start'},
            'stop' : { method:'GET', url:'api/plugins/:id/stop'},
            'install': {method:'POST',url:'api/plugins/install'},
            'secret': {
                method:'GET',
                url:'api/plugins/:id/secret',
                transformResponse: function(data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
