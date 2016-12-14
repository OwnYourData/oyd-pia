'use strict';

angular.module('piaApp')
        .config(function ($stateProvider) {
            $stateProvider
                    .state('data', {
                        parent: 'entity',
                        url: '/data',
                        data: {
                            authorities: ['ROLE_USER'],
                            pageTitle: 'global.title'
                        },
                        views: {
                            'content@': {
                                templateUrl: 'scripts/app/entities/data/data.html',
                                controller: 'DataController'
                            }
                        },
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                    $translatePartialLoader.addPart('data');
                                    $translatePartialLoader.addPart('repo');
                                    $translatePartialLoader.addPart('item');
                                    $translatePartialLoader.addPart('global');
                                    return $translate.refresh();
                                }]
                        }
                    })
                    .state('data.item', {
                        parent: 'data',
                        url: '/{repositoryId}',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        views: {
                            'content@': {
                                templateUrl: 'scripts/app/entities/data/data-items.html',
                                controller: 'DataItemsController'
                            }
                        },
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                    $translatePartialLoader.addPart('repo');
                                    $translatePartialLoader.addPart('data');
                                    return $translate.refresh();
                                }]
                        }
                    })
                    .state('data.item.detail', {
                        parent: 'data.item',
                        url: '/{itemId}',
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
                            entity: ['$stateParams', 'Item', function ($stateParams, Item) {
                                    return Item.get({id: $stateParams.itemId});
                                }]
                        }
                    })
                    .state('data.item.edit', {
                        parent: 'data.item',
                        url: '/edit/{itemId}',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                                $uibModal.open({
                                    templateUrl: 'scripts/app/entities/item/item-dialog.html',
                                    controller: 'ItemDialogController',
                                    size: 'lg',
                                    resolve: {
                                        entity: ['Item', function (Item) {
                                                return Item.get({id: $stateParams.itemId});
                                            }]
                                    }
                                }).result.then(function (result) {
                                    $state.go('data.item', {repositoryId: $stateParams.repositoryId}, {reload: true});
                                }, function () {
                                    $state.go('data.item', {repositoryId: $stateParams.repositoryId});
                                })
                            }]
                    })
                    .state('data.item.delete', {
                        parent: 'data.item',
                        url: '/delete/{itemId}',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                                $uibModal.open({
                                    templateUrl: 'scripts/app/entities/item/item-delete-dialog.html',
                                    controller: 'ItemDeleteController',
                                    size: 'md',
                                    resolve: {
                                        entity: ['Item', function (Item) {
                                                return Item.get({id: $stateParams.itemId});
                                            }]
                                    }
                                }).result.then(function (result) {
                                    $state.go('data.item', {repositoryId: $stateParams.repositoryId}, {reload: true});
                                }, function () {
                                    $state.go('data.item', {repositoryId: $stateParams.repositoryId});
                                })
                            }]
                    })
                    .state('data.new', {
                        parent: 'data',
                        url: '/new',
                        data: {
                            authorities: ['ROLE_USER'],
                        },
                        onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                                $uibModal.open({
                                    templateUrl: 'scripts/app/entities/repo/repo-dialog.html',
                                    controller: 'RepoDialogController',
                                    size: 'lg',
                                    resolve: {
                                        entity: function () {
                                            return {
                                                identifier: null,
                                                description: null,
                                                creator: null,
                                                id: null
                                            };
                                        }
                                    }
                                }).result.then(function (result) {
                                    $state.go('data', null, {reload: true});
                                }, function () {
                                    $state.go('data');
                                })
                            }]
                    });
        })