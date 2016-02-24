'use strict';

angular.module('mainApp')
    .controller('TransactionsController', function ($rootScope, $scope, $state, $timeout, $http, $log, $modal, TransactionService) {

        $scope.records = [];

        TransactionService.findAll().then(function (response) {
            $log.info("Found " + response.length + " records");
            $scope.records = response;
        });

        // Table sorting
        $scope.sortType = 'id'; // set the default sort type
        $scope.sortReverse = false;  // set the default sort order
        $scope.searchRecord = '';     // set the default search/filter term

        $scope.openModal = function (transaction) {
            var modalInstance = $modal.open({
                templateUrl: 'transactionTemplate.html',
                controller: 'TransactionTemplateController',
                resolve: {
                    transaction: function () {
                        return transaction;
                    }
                }
            });
            modalInstance.result.then(function (cb) {
                return cb;
            }, function () {
                $log.info('Modal for transaction template dismissed at: ' + new Date());
            });
        };

    });


angular.module('mainApp').controller('TransactionTemplateController', function ($rootScope, $scope, $state, $timeout, $modal, $modalInstance, $http, $log, transaction) {
    $log.info("Opening transaciton template controller....");
    $scope.record = transaction;

    $scope.status = 0;

    var getStatus = function () {
        if ($scope.record.parkingStatus == null) {
            return 0;
        }
        if ($scope.record.parkingStatus.indexOf("Parked") > -1) {
            return 0;
        }
        if ($scope.record.parkingStatus.indexOf("Finished Parking") > -1) {
            return 1;
        }
        if ($scope.record.parkingStatus.indexOf("COMPLETED") > -1) {
            return 2;
        }
        if ($scope.record.parkingStatus.indexOf("EXCEPTION") > -1 || $scope.record.parkingStatus.indexOf("ERROR") > -1) {
            return -1;
        }
        return 0;
    };


    $scope.openGateModal = function (transaction) {
        var modalInstance = $modal.open({
            templateUrl: 'openGateTemplate.html',
            controller: 'OpenGateController',
            resolve: {
                transaction: function () {
                    return transaction;
                }
            }
        });
        modalInstance.result.then(function (cb) {
            return cb;
        }, function () {
            $log.info('Modal for transaction template dismissed at: ' + new Date());
        });
    };


    $scope.openGate = function () {
        $scope.cancel();
        $scope.openGateModal($scope.record);
    };

    $scope.status = getStatus();

    $scope.ok = function () {
        $modalInstance.close(function () {
            $log.info("Closing modal instance.");
        });
    };
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

});


angular.module('mainApp').controller('OpenGateController', function ($rootScope, $scope, $state, $timeout, $modal, $modalInstance, $http, $log, transaction) {
    $scope.record = transaction;

    $scope.open = function () {
        if (!confirm("Are you sure you want to open the gate for transaction id = " + $scope.record.id + " ?")) {
            return;
        }
        alert("Succesfully opened gated.");
    };


    $scope.ok = function () {
        $modalInstance.close(function () {
            $log.info("Closing modal instance.");
        });
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

});
