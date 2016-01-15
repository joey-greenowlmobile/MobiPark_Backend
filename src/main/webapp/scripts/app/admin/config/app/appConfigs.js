'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('appConfigs', {
                parent: 'admin',
                url: '/admin/configs',
                data: {
                    roles: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/config/app/app_configs.html',
                        controller: 'AppConfigController'
                    }
                }
            });
    });
