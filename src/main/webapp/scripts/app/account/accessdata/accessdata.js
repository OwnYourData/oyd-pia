'use strict';

angular.module('piaApp')
        .config(function ($stateProvider) {
            $stateProvider
                    .state('accessdata', {
                        parent: 'account',
                        url: '/accessdata',
                        data: {
                            authorities: ['ROLE_USER'],
                            pageTitle: 'global.title'
                        },
                        views: {
                            'content@': {
                                templateUrl: 'scripts/app/account/accessdata/accessdata.html',
                                controller: 'PasswordController'
                            }
                        },
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                    $translatePartialLoader.addPart('password');
                                    $translatePartialLoader.addPart('accessdata');
                                    return $translate.refresh();
                                }]
                        }
                    });
        });