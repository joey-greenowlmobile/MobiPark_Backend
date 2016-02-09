'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sales', {
                parent: 'operation',
                url: '/sales',
                data: {
                    roles: ['ROLE_OPERATOR']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/operation/sales/sales.html',
                        controller: 'SalesController'
                    }
                }
            });
    });
