'use strict';

angular.module('piaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('datatype', {
                parent: 'entity',
                url: '/datatypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.datatype.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/datatype/datatypes.html',
                        controller: 'DatatypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('datatype');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('datatype.detail', {
                parent: 'entity',
                url: '/datatype/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.datatype.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/datatype/datatype-detail.html',
                        controller: 'DatatypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('datatype');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Datatype', function($stateParams, Datatype) {
                        return Datatype.get({id : $stateParams.id});
                    }]
                }
            })
            .state('datatype.new', {
                parent: 'datatype',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/datatype/datatype-dialog.html',
                        controller: 'DatatypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('datatype', null, { reload: true });
                    }, function() {
                        $state.go('datatype');
                    })
                }]
            })
            .state('datatype.edit', {
                parent: 'datatype',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/datatype/datatype-dialog.html',
                        controller: 'DatatypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Datatype', function(Datatype) {
                                return Datatype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('datatype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('datatype.delete', {
                parent: 'datatype',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/datatype/datatype-delete-dialog.html',
                        controller: 'DatatypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Datatype', function(Datatype) {
                                return Datatype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('datatype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
