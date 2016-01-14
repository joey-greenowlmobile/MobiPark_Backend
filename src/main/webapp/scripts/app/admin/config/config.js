'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('configs', {
                parent: 'admin',
                url: '/admin/configs',
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/config/configs.html',
                        controller: 'ConfigController'
                    }
                }
            });
    });
