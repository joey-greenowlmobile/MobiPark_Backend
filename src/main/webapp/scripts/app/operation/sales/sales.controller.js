'use strict';

angular.module('mainApp')
    .controller('SalesController', function ($rootScope, $scope, $state, $timeout, $http, $log, $modal, SalesService) {

        $scope.records = [];

        SalesService.findAll().then(function (response) {
            $log.info("Found " + response.length + " records");
            $scope.records = response;
        });

        // Table sorting
        $scope.sortType = 'id'; // set the default sort type
        $scope.sortReverse = false;  // set the default sort order
        $scope.searchRecord = '';     // set the default search/filter term

        $scope.openModal = function (sale) {
            var modalInstance = $modal.open({
                templateUrl: 'saleTemplate.html',
                controller: 'SaleTemplateController',
                resolve: {
                    sale: function () {
                        return sale;
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


angular.module('mainApp').controller('SaleTemplateController', function ($rootScope, $scope, $state, $timeout, $modal, $modalInstance, $http, $log, sale) {
    $log.info("Opening sale template controller....");
    $scope.record = sale;

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
