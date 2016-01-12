'use strict';

angular.module('mainApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('profile', {
                parent: 'site',
                url: '/profile/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profile.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/profile/profile.html',
                        controller: 'UserProfileController'
                    }
                }
            });
    });
