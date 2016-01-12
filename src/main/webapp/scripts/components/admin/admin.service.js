'use strict';

angular.module('mainApp')
    .factory('AdminService', function ($rootScope, API_VERSION, $http) {
        return {
            findAll: function () {
                return $http.get('api/' + API_VERSION + '/users/profiles/public').then(function (response) {
                    return response.data;
                });
            },
            deleteUser: function (login) {
                var promise = $http.delete('api/' + API_VERSION + '/account/' + login).then(function (response) {
                });
                return promise;
            }
        };
    });
