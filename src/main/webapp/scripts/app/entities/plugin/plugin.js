'use strict';

angular.module('piaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('plugin', {
                parent: 'entity',
                url: '/plugins',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.plugin.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/plugin/plugins.html',
                        controller: 'PluginController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('plugin');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('plugin.detail', {
                parent: 'entity',
                url: '/plugin/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.plugin.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/plugin/plugin-detail.html',
                        controller: 'PluginDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('plugin');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Plugin', function($stateParams, Plugin) {
                        return Plugin.get({id : $stateParams.id});
                    }]
                }
            })
            .state('plugin.edit', {
                parent: 'plugin',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/plugin/plugin-dialog.html',
                        controller: 'PluginDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Plugin', function(Plugin) {
                                return Plugin.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('plugin', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('plugin.delete', {
                parent: 'plugin',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/plugin/plugin-delete-dialog.html',
                        controller: 'PluginDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Plugin', function(Plugin) {
                                return Plugin.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('plugin', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('plugin.register', {
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/plugin/plugin-register-dialog.html',
                        controller: 'PluginRegisterDialogController',
                        size: 'lg',
                    }).result.then(function(result) {
                        $state.go('plugin', null, { reload: true });
                    }, function() {
                        $state.go('plugin', null, { reload: true });
                    })
                }]
            })
            .state('plugin.install', {
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/plugin/plugin-install-dialog.html',
                        controller: 'PluginInstallDialogController',
                        size: 'lg',
                    }).result.then(function(result) {
                        $state.go('plugin', null, { reload: true });
                    }, function() {
                        $state.go('plugin', null, { reload: true });
                    })
                }]
            });
    });
