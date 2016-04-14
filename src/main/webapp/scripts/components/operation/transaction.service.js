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
            },
            enter: function () {
            	var url = 'api/' + API_VERSION + '/parking/manualEnter';
            	var postData = {"gateId": "1","gateDistance": "1",  "lotId": "1"};            	
            	$http.post(url, postData).success(function(response){
            		return response.data;
            	}).error(function(data,status,headers){
            		return response.data;
            	});            	
            },
            exit: function () {
            	var url = 'api/' + API_VERSION + '/parking/manualExit';
            	var postData = {"gateId": "2","gateDistance": "1",  "lotId": "1"};            	
            	$http.post(url, postData).success(function(response){
            		return response.data;
            	}).error(function(data,status,headers){
            		return response.data;
            	});            	
            }
        };
    });
