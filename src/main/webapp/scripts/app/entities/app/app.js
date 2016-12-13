'use strict';

angular.module('piaApp')
        .config(function ($stateProvider) {
            $stateProvider
                    .state('app', {
                        parent: 'entity',
                        url: '/',
                        data: {
                            authorities: [],
                            pageTitle: 'global.title'
                        },
                        views: {
                            'content@': {
                                templateUrl: 'scripts/app/entities/app/apps.html',
                                controller: 'AppController'
                            }
                        },
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                    $translatePartialLoader.addPart('main');
                                    $translatePartialLoader.addPart('app');
                                    $translatePartialLoader.addPart('plugin');
                                    $translatePartialLoader.addPart('store');
                                    $translatePartialLoader.addPart('global');
                                    return $translate.refresh();
                                }]
                        }
                    })
                    .state('edit', {
                        parent: 'app',
                        url: '/{id}/edit',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                                $uibModal.open({
                                    templateUrl: 'scripts/app/entities/plugin/plugin-dialog.html',
                                    controller: 'PluginDialogController',
                                    size: 'lg',
                                    resolve: {
                                        entity: ['Plugin', function (Plugin) {
                                                return Plugin.get({id: $stateParams.id});
                                            }]
                                    }
                                }).result.then(function (result) {
                                    $state.go('app', null, {reload: true});
                                }, function () {
                                    $state.go('^');
                                })
                            }]
                    })
                    .state('delete', {
                        parent: 'app',
                        url: '/{id}/delete',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                                $uibModal.open({
                                    templateUrl: 'scripts/app/entities/plugin/plugin-delete-dialog.html',
                                    controller: 'PluginDeleteController',
                                    size: 'md',
                                    resolve: {
                                        entity: ['Plugin', function (Plugin) {
                                                return Plugin.get({id: $stateParams.id});
                                            }]
                                    }
                                }).result.then(function (result) {
                                    $state.go('app', null, {reload: true});
                                }, function () {
                                    $state.go('^');
                                })
                            }]
                    })
                    .state('app.new', {
                        parent: 'app',
                        url: '/new',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                                $uibModal.open({
                                    templateUrl: 'scripts/app/entities/store/store-dialog.html',
                                    controller: 'StoreDialogController',
                                    size: 'lg',
                                    resolve: {
                                        entity: function () {
                                            return {
                                                name: null,
                                                url: null,
                                                id: null
                                            };
                                        }
                                    }
                                }).result.then(function (result) {
                                    $state.go('app', null, {reload: true});
                                }, function () {
                                    $state.go('app');
                                })
                            }]
                    })
                    .state('app.detail', {
                        parent: 'entity',
                        url: '/app/{id}',
                        data: {
                            authorities: ['ROLE_USER'],
                            pageTitle: 'piaApp.store.detail.title'
                        },
                        views: {
                            'content@': {
                                templateUrl: 'scripts/app/entities/store/store-detail.html',
                                controller: 'StoreDetailController'
                            }
                        },
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                    $translatePartialLoader.addPart('store');
                                    return $translate.refresh();
                                }],
                            entity: ['$stateParams', 'Store', function ($stateParams, Store) {
                                    return Store.get({id: $stateParams.id});
                                }]
                        }
                    })
                    .state('app.edit', {
                        parent: 'app',
                        url: '/{id}/editstore',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                                $uibModal.open({
                                    templateUrl: 'scripts/app/entities/store/store-dialog.html',
                                    controller: 'StoreDialogController',
                                    size: 'lg',
                                    resolve: {
                                        entity: ['Store', function (Store) {
                                                return Store.get({id: $stateParams.id});
                                            }]
                                    }
                                }).result.then(function (result) {
                                    $state.go('app', null, {reload: true});
                                }, function () {
                                    $state.go('^');
                                })
                            }]
                    })
                    .state('app.delete', {
                        parent: 'app',
                        url: '/{id}/deletestore',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                                $uibModal.open({
                                    templateUrl: 'scripts/app/entities/store/store-delete-dialog.html',
                                    controller: 'StoreDeleteController',
                                    size: 'md',
                                    resolve: {
                                        entity: ['Store', function (Store) {
                                                return Store.get({id: $stateParams.id});
                                            }]
                                    }
                                }).result.then(function (result) {
                                    $state.go('app', null, {reload: true});
                                }, function () {
                                    $state.go('^');
                                })
                            }]
                    });
        });