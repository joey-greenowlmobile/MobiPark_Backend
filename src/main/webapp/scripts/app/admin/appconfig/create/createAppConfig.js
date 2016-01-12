'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('createAppConfig', {
                parent: 'admin',
                url: '/app/config/new',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'configuration.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/appconfig/create/createAppConfig.html',
                        controller: 'CreateAppConfigController'
                    }
                }
            });
    });
