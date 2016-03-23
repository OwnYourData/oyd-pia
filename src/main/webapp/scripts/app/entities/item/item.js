'use strict';

angular.module('piaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('item', {
                parent: 'entity',
                url: '/items',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.item.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/item/items.html',
                        controller: 'ItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('item');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('item.detail', {
                parent: 'entity',
                url: '/item/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.item.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/item/item-detail.html',
                        controller: 'ItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('item');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Item', function($stateParams, Item) {
                        return Item.get({id : $stateParams.id});
                    }]
                }
            })
            .state('item.new', {
                parent: 'item',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/item/item-dialog.html',
                        controller: 'ItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('item', null, { reload: true });
                    }, function() {
                        $state.go('item');
                    })
                }]
            })
            .state('item.edit', {
                parent: 'item',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/item/item-dialog.html',
                        controller: 'ItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Item', function(Item) {
                                return Item.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('item', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('item.delete', {
                parent: 'item',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/item/item-delete-dialog.html',
                        controller: 'ItemDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Item', function(Item) {
                                return Item.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('item', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
