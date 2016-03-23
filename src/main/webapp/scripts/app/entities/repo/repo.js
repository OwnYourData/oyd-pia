'use strict';

angular.module('piaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('repo', {
                parent: 'entity',
                url: '/repos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.repo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/repo/repos.html',
                        controller: 'RepoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('repo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('repo.detail', {
                parent: 'entity',
                url: '/repo/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'piaApp.repo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/repo/repo-detail.html',
                        controller: 'RepoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('repo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Repo', function($stateParams, Repo) {
                        return Repo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('repo.new', {
                parent: 'repo',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
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
                    }).result.then(function(result) {
                        $state.go('repo', null, { reload: true });
                    }, function() {
                        $state.go('repo');
                    })
                }]
            })
            .state('repo.edit', {
                parent: 'repo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/repo/repo-dialog.html',
                        controller: 'RepoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Repo', function(Repo) {
                                return Repo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('repo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('repo.delete', {
                parent: 'repo',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/repo/repo-delete-dialog.html',
                        controller: 'RepoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Repo', function(Repo) {
                                return Repo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('repo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
