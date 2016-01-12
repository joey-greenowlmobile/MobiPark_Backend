'use strict';

angular.module('mainApp')
    .controller('UserProfileController', function ($rootScope, $scope, $stateParams) {
        $scope.parm = $stateParams;
        $scope.id = $stateParams.id;
    });
