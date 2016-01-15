'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('runtimeConfig', {
                parent: 'admin',
                url: '/config/runtime',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Manage Runtime Configuration'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/config/runtime/runtimeConfig.html',
                        controller: 'RuntimeConfigurationController'
                    }
                }
            });
    });

