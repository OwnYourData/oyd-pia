'use strict';

angular.module('piaApp')
    .controller('PluginInstallDialogController', function($scope, $uibModalInstance,Plugin,Store) {

        $scope.stores = [];

        $scope.loadAll = function() {
            Store.query(function(result) {
                for (var i = 0; i < result.length; i++) {
                    $scope.stores.push(result[i]);
                    $scope.loadPlugins(result[i]);
                }
            });
        };

        $scope.loadPlugins = function(store) {
            store.plugins = [];

            Store.plugins({id: store.id},function(result) {
                for (var i = 0; i < result.length; i++) {
                   store.plugins.push(result[i]);
                }
            })
        };

        $scope.install = function(store,plugin) {
            Plugin.install({id: store.id, plugin:plugin.id});
            $uibModalInstance.dismiss('install');
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.loadAll();

    });

