'use strict';

angular.module('mainApp')
    .factory('Password', function ($resource, API_VERSION) {
        return $resource('api/' + API_VERSION + '/account/change_password', {}, {
        });
    });
