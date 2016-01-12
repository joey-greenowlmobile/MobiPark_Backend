'use strict';

angular.module('mainApp')
    .factory('Account', function Account($resource, API_VERSION) {
        return $resource('api/' + API_VERSION + '/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });
    });
