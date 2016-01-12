'use strict';

angular.module('mainApp')
    .controller('CreateAppConfigController', function ($http,$scope) {
        $scope.types = null;
        $scope.loading = false;
        $scope.selectedType = null;

        $scope.getTypes  = function(){
            $scope.loading = true;
            $http.get('api/admin/config/types').
                success(function(data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    $scope.types = data;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    alert("Unable to fetch Configuration types!");
                }).finally(function(){
                    $scope.loading = false;
                });
        };

        $scope.selectType = function(t){
            $scope.selectedType = t;
        };

        $scope.getTypes();
    });
