'use strict';

angular.module('mainApp')
    .controller('TransactionsController', function ($rootScope, $scope, $state, $timeout, $http, $log, Auth) {

        $scope.dummy = [];

        $http.get('api/v1/transactions').then(function (response) {
            $scope.dummy = response.data;
        });

        $(document).ready(function () {
            $('#example').DataTable();
        });
    });
