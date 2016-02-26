'use strict';

angular.module('piaApp')
    .controller('PluginDetailController', function ($scope, $rootScope, $stateParams, entity, Plugin) {
        $scope.plugin = entity;
        $scope.load = function (id) {
            Plugin.get({id: id}, function(result) {
                $scope.plugin = result;
            });
        };
        var unsubscribe = $rootScope.$on('piaApp:pluginUpdate', function(event, result) {
            $scope.plugin = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
