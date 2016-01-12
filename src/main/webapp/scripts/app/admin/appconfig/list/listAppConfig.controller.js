'use strict';

angular.module('mainApp')
    .controller('ListAppConfigController', function ($http,$scope) {
        $scope.configs = null;
        $scope.loading = false;
        $scope.getConfigs  = function(){
            $scope.loading = true;
            $http.get('api/config').
                success(function(data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    $scope.configs = data;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    alert("Unable to fetch Configurations!");
                }).finally(function(){
                    $scope.loading = false;
                });
        };

        $scope.getConfigs();
    });
