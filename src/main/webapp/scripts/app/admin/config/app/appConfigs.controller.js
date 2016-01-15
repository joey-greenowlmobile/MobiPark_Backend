'use strict';

angular.module('mainApp')
    .controller('AppConfigController', function ($rootScope, $scope, $http, $modal) {

        $scope.events = [];

        $scope.getAppConfigs = function () {
            $scope.loading = true;
            $http.get('api/admin/configs').success(function (response) {
                $scope.configs = response;
            }).finally(function (response) {
                $scope.loading = false;
            });
        };

        $scope.edit = function (config) {
            var modalInstance = $modal.open({
                templateUrl: 'display_config.html',
                controller: 'ViewAppConfigController',
                size: null,
                resolve: {
                    config: function () {
                        return config;
                    }
                }
            });

            modalInstance.result.then(function () {
                $scope.getAppConfigs();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };


        $scope.create = function () {
            var modalInstance = $modal.open({
                templateUrl: 'create_config.html',
                controller: 'CreateAppConfigController',
            });

            modalInstance.result.then(function () {
                $scope.getAppConfigs();
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.getAppConfigs();
    });


angular.module('mainApp')
    .controller('ViewAppConfigController', function ($scope, $modalInstance, $http, config) {
        $scope.config = config;

        $scope.save = function () {
            $http.post('api/admin/config', $scope.config).success(function (response) {
                alert("Updated");
            }).finally(function (response) {
                $scope.loading = false;
                $modalInstance.close();
            });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });


angular.module('mainApp')
    .controller('CreateAppConfigController', function ($scope, $modalInstance, $http, config) {
        $scope.save = function () {
            $http.post('api/admin/config', $scope.config).success(function (response) {
                alert("Saved");
                ;
            }).finally(function (response) {
                $scope.loading = false;
            });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
