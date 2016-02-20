'use strict';

angular.module('piaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('data', {
                parent: 'entity',
                url: '/datas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.data.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/data/datas.html',
                        controller: 'DataController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('data');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('data.detail', {
                parent: 'entity',
                url: '/data/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.data.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/data/data-detail.html',
                        controller: 'DataDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('data');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Data', function($stateParams, Data) {
                        return Data.get({id : $stateParams.id});
                    }]
                }
            })
            .state('data.new', {
                parent: 'data',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/data/data-dialog.html',
                        controller: 'DataDialogController',
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
                        $state.go('data', null, { reload: true });
                    }, function() {
                        $state.go('data');
                    })
                }]
            })
            .state('data.edit', {
                parent: 'data',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/data/data-dialog.html',
                        controller: 'DataDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Data', function(Data) {
                                return Data.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('data', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('data.delete', {
                parent: 'data',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/data/data-delete-dialog.html',
                        controller: 'DataDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Data', function(Data) {
                                return Data.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('data', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
