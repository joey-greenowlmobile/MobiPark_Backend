'use strict';

angular.module('mainApp')
    .controller('TransactionsController', function ($rootScope, $scope, $state, $timeout, $http, $log, $modal, TransactionService) {
        
        $scope.records = [];        
        $scope.loading = false;
        $scope.inFlight = false;
           
        $scope.init = function () {
            $log.info("Initializing Scope for controller = TransactionsController");
            $scope.dt = new Date();
            
            $("#date").datepicker({
            	dateFormat: 'yy-mm-dd',onSelect: function (dateText) {
                    $log.info("Selected.");
                    var dateAsString = dateText + "T00:00:00-04:00";                    
                    $scope.dt = new Date(dateAsString);                     
                    var timestamp = $scope.dt.getTime();
                    $log.info("date is "+dateAsString);
                    $scope.$apply();
                    $scope.getRecordsByDate();
                }
            });

        };

        $scope.updateDate = function () {
            var newVal = angular.element(document.querySelector('#date')).val();
            $log.info("new date is " + newVal);
            $scope.dt = newVal;
        };

        $scope.getRecords = function () {
            $scope.getAllRecords();
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
            
        };

        $scope.getRecordsByDate = function () {
        	
        	var dateSelected = $scope.dt;
        	$log.info("start :"+dateSelected);
        	dateSelected.setHours(0, 0, 0, 1);
        	var startTimeStamp = dateSelected.getTime();
        	$log.info("starttime:"+startTimeStamp);
        	
        	dateSelected.setHours(23, 59, 59, 999);
        	var endTimeStamp = dateSelected.getTime();
            $scope.loading = true;
            var type;
            if ($scope.parking.status == "1") {
                type = "COMPLETED";
            }
            else if ($scope.parking.status == "2") {
                type = "IN_FLIGHT";
            }
            else if($scope.parking.status == "3"){
            	type = "PENDING_ENTER";
            }
            else if($scope.parking.status == "4"){
            	type = "PENDING_EXIT";
            }
            else if($scope.parking.status == "5"){
            	type = "ALARM_ENTER";
            }
            else if($scope.parking.status == "6"){
            	type = "ALARM_EXIT";
            }
            $log.info("type:"+type);
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

            if (status.indexOf("PENDING") > -1 || status.indexOf("ALARM") > -1) {
                return 1;
            }
            if (status.indexOf("IN_FLIGHT") > -1) {
            	return 2;
            }
            return 3;

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
        $scope.dt = new Date();
        $scope.parking={status:'0'};      
        $log.info("parking status:"+$scope.parking.status);        
        $scope.getRecordsByDate();

    });


angular.module('mainApp').controller('TransactionTemplateController', function ($rootScope, $scope, $state, $timeout, $modal, $modalInstance, $http, $log, transaction) {
    $log.info("Opening transaciton template controller....");
    $scope.record = transaction;

    $scope.status = 0;

    var getStatus = function () {        
        if ($scope.record.parkingStatus.indexOf("IN_FLIGHT") > -1) {
            return 2;
        }
        if ($scope.record.parkingStatus.indexOf("PENDING") > -1 || $scope.record.parkingStatus.indexOf("ALARM") > -1) {
            return 1;
        }
        if ($scope.record.parkingStatus.indexOf("COMPLETED") > -1) {
            return 0;
        }
        if ($scope.record.parkingStatus.indexOf("EXCEPTION") > -1) {
            return -1;
        }
        return 3;
    };


    $scope.openGateModal = function (transaction,gid) {
        var modalInstance = $modal.open({
            templateUrl: 'openGateTemplate'+gid+'.html',
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

    $scope.openGate1 = function () {
        $scope.cancel();
        $scope.openGateModal($scope.record,1);
    };
    
    $scope.openGate2 = function () {
        $scope.cancel();
        $scope.openGateModal($scope.record,2);
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
   
    $scope.statusLogs = transaction.statusLogs;
    $log.info("status logs:"+$scope.statusLogs);
});


angular.module('mainApp').controller('OpenGateController', function ($rootScope, $scope, $state, $timeout, $modal, $modalInstance, $http, $log, transaction, TransactionService) {
    $scope.record = transaction;

    $scope.open1 = function () {
        if (!confirm("Are you sure you want to open the gate for transaction id = " + $scope.record.id + " ?")) {
            return;
        }
        TransactionService.enter(function(response){
        	alert("The gate opened successfully");        	
        });
        $modalInstance.close();
    };
    
    $scope.open2 = function () {
        if (!confirm("Are you sure you want to open the gate for transaction id = " + $scope.record.id + " ?")) {
            return;
        }
        TransactionService.exit(function(response){
        	alert("The gate opened successfully");
        });
        $modalInstance.close();
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
