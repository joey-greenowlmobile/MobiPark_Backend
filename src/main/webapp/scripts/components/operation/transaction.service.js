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

                var formatDate = function (dateToFormat) {
                    if (dateToFormat !== undefined && !angular.isString(dateToFormat)) {
                        return dateToFormat.getYear() + '-' + dateToFormat.getMonth() + '-' + dateToFormat.getDay();
                    }
                    return dateToFormat;
                };

                return $http.get('api/audits/byDates', {
                    params: {
                        fromDate: formatDate(fromDate),
                        toDate: formatDate(toDate)
                    }
                }).then(function (response) {
                    return response.data;
                });
            }
        };
    });
