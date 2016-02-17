'use strict';

angular.module('mainApp')
    .controller('TransactionsController', function ($rootScope, $scope, $state, $timeout, $http, $log, TransactionService) {

        $scope.records = [];

        TransactionService.findAll().then(function (response) {
            $log.info("Found " + response.length + " records");
            $scope.records = response;
        });

        $(document).ready(function () {
            $('#transactions_data_table').DataTable();
        });
    });
