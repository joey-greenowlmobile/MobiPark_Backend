'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('transactions', {
                parent: 'operation',
                url: '/transactions',
                data: {
                    roles: ['ROLE_OPERATOR']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/operation/transactions/transactions.html',
                        controller: 'TransactionsController'
                    }
                }
            });
    });
