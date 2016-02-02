'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('operation', {
                abstract: true,
                parent: 'site'
            });
    });
