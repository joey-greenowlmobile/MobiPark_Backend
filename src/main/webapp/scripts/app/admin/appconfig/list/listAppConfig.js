'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('listAppConfig', {
                parent: 'admin',
                url: '/app/config',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'configuration.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/appconfig/list/listAppConfig.html',
                        controller: 'ListAppConfigController'
                    }
                }
            });
    });
