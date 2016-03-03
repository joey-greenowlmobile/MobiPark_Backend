'use strict';

angular.module('mainApp')
    .factory('TransactionService', function ($http, API_VERSION) {
        return {
            findAll: function () {
                return $http.get('api/' + API_VERSION + '/parking/records').then(function (response) {
                    return response.data;
                });
            },
            findByDateAndType: function (fromDate, toDate) {

                return $http.get('api/' + API_VERSION + '/parking/records?start=' + fromDate + "&end=" + toDate).then(function (response) {
                    return response.data;
                });
            }
        };
    });
