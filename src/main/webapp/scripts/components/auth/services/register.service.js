'use strict';

angular.module('mainApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


