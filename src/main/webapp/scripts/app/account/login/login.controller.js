'use strict';

angular.module('mainApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, $http, $log, Auth) {
        $scope.user = {};
        $scope.errors = {};
        $scope.rememberMe = true;
        $scope.loading = false;

        $timeout(function () {
            angular.element('[ng-model="username"]').focus();
        });

        $scope.login = function () {
            $scope.loading = true;
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                $scope.loading = false;
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
            }).catch(function () {
                $scope.authenticationError = true;
                $scope.loading = false;
            });

        };
    });
