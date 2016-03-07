'use strict';

angular.module('mainApp')
    .factory('TransactionService', function ($http, API_VERSION) {
        return {
            findAll: function (type) {
                var url = 'api/' + API_VERSION + '/parking/records';
                if (type) {
                    url += '?type=' + type;
                }
                return $http.get(url).then(function (response) {
                    return response.data;
                });
            },
            findByDateAndType: function (fromDate, toDate, type) {
                var url = 'api/' + API_VERSION + '/parking/records?start=' + fromDate + "&end=" + toDate;
                if (type) {
                    url += "&type=" + type;
                }
                return $http.get(url).then(function (response) {
                    return response.data;
                });
            }
        };
    });
