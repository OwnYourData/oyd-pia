/**
 * Created by michael on 23.02.16.
 */
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
    });
