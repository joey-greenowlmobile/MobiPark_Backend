'use strict';

angular.module('mainApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
