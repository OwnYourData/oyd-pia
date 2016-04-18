'use strict';

angular.module('piaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('store', {
                parent: 'entity',
                url: '/stores',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.store.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/store/stores.html',
                        controller: 'StoreController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('store');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('store.detail', {
                parent: 'entity',
                url: '/store/{id}',
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
                    entity: ['$stateParams', 'Store', function($stateParams, Store) {
                        return Store.get({id : $stateParams.id});
                    }]
                }
            })
            .state('store.new', {
                parent: 'store',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
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
                    }).result.then(function(result) {
                        $state.go('store', null, { reload: true });
                    }, function() {
                        $state.go('store');
                    })
                }]
            })
            .state('store.edit', {
                parent: 'store',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/store/store-dialog.html',
                        controller: 'StoreDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Store', function(Store) {
                                return Store.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('store', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('store.delete', {
                parent: 'store',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/store/store-delete-dialog.html',
                        controller: 'StoreDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Store', function(Store) {
                                return Store.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('store', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
