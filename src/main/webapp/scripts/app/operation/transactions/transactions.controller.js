'use strict';

angular.module('mainApp')
    .controller('TransactionsController', function ($rootScope, $scope, $state, $timeout, $http, $log, $modal, TransactionService) {

        $scope.records = [];

        $scope.daily = true;
        $scope.loading = false;
        $scope.inFlight = false;
        $scope.dt = new Date();
        $scope.init = function () {
            $log.info("Initializing Scope for controller = TransactionsController");
            $("#date").datepicker({
                onSelect: function (dateText, inst) {
                    $log.info("Selected.");
                    var dateAsString = dateText; //the first parameter of this function
                    $log.info("New Date is " + dateAsString);
                    $scope.dt = new Date(dateAsString);
                    var timestamp = $scope.dt.getTime();
                    $scope.$apply();
                }
            });

        };

        $scope.updateDate = function () {
            var newVal = angular.element(document.querySelector('#date')).val();
            $log.info("new date is " + newVal);
            $scope.dt = newVal;
        };

        $scope.getRecords = function () {
            $log.info("Fetching records. Daily = ", $scope.daily);
            $scope.daily ? $scope.getDailyRecords() : $scope.getAllRecords();
        };

        $scope.getTitle = function () {
            if ($scope.daily) {
                if ($scope.inFlight) {
                    return "Daily In Flight Transactions";
                }
                return "Daily Transactions";
            }
            if ($scope.inFlight) {
                return "All In Flight Transactions";
            }
            return "All Transactions";
        };

        $scope.getAllRecords = function () {
            $scope.loading = true;
            var type;
            if ($scope.inFlight) {
                type = "IN_FLIGHT";
            }
            TransactionService.findAll(type).then(function (response) {
                $log.info("Found " + response.length + " records");
                $scope.records = response;
                for (var i = 0; i < $scope.records.length; i++) {
                    $scope.records[i].pType = $scope.getType($scope.records[i].parkingStatus);
                }
                $scope.loading = false;
            });
            $scope.daily = false;
        };

        $scope.getRecordsByDate = function (startTimeStamp, endTimeStamp) {
            $scope.loading = true;
            var type;
            if ($scope.inFlight) {
                type = "IN_FLIGHT";
            }
            TransactionService.findByDateAndType(startTimeStamp, endTimeStamp, type).then(function (response) {
                $log.info("Found " + response.length + " records");
                $scope.records = response;
                for (var i = 0; i < $scope.records.length; i++) {
                    $scope.records[i].pType = $scope.getType($scope.records[i].parkingStatus);
                }
                $scope.loading = false;
            });
            $scope.daily = false;
        };

        $scope.getDailyRecords = function () {
            $scope.loading = true;
            var type;
            if ($scope.inFlight) {
                type = "IN_FLIGHT";
            }
            var start = new Date();
            start.setHours(0, 0, 0, 0);
            var estStart = start.getTime();

            var end = new Date();
            end.setHours(23, 59, 59, 999);
            var estEnd = end.getTime();
            TransactionService.findByDateAndType(estStart, estEnd, type).then(function (response) {
                $log.info("Found " + response.length + " records");
                $scope.records = response;
                for (var i = 0; i < $scope.records.length; i++) {
                    $scope.records[i].pType = $scope.getType($scope.records[i].parkingStatus);
                }
                $scope.loading = false;
            });

            $scope.daily = true;

        };

        // Table sorting
        $scope.sortType = 'id'; // set the default sort type
        $scope.sortReverse = true;  // set the default sort order
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

        $scope.getUpdatedTime = function (t) {
            if (t != null) {
                return t - (5 * 60 * 60 * 1000);
            }
            return t;
        };

        $scope.getType = function (status) {
            if (status.indexOf("EXCEPTION") > -1) {
                return -1;
            }

            if (status.indexOf("COMPLETED") > -1) {
                return 0;
            }

            if (status.indexOf("PENDING") > -1) {
                return 1;
            }
            return 2;

        };

        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();

        if (dd < 10) {
            dd = '0' + dd
        }

        if (mm < 10) {
            mm = '0' + mm
        }

        $scope.today = mm + '/' + dd + '/' + yyyy;
        $scope.getRecords();

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

    $scope.getUpdatedTime = function (t) {
        if (t != null) {
            return t - (5 * 60 * 60 * 1000);
        }
        return t;
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

    $scope.getUpdatedTime = function (t) {
        if (t != null) {
            return t - (5 * 60 * 60 * 1000);
        }
        return t;
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
