'use strict';

angular.module('mainApp')
    .factory('Verify', function ($resource) {
        return $resource('api/verify', {}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });
    });


