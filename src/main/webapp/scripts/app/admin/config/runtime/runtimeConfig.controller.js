'use strict';
angular.module('mainApp')
    .controller('RuntimeConfigurationController', function ($scope, Principal, $http, $log) {

        $scope.response = null;
        $scope.successMsg = "";

        $scope.fmt = "xml";

        /**
         * Retrieve the rule from server.
         */
        $scope.fetchFile = function () {
            $scope.loading = true;
            $http({
                method: 'GET',
                url: "pub/runtime_configs"
            }).success(function (data, status, headers, config) {
                $scope.original = data;
                $scope.response = data;
                $scope.showEditor();
            }).error(function (data, status, headers, config) {
                alert("Unable to obtain runtime configuration file.");
            });
        };

        /**
         * Show the xml file to be edited.
         */
        $scope.showEditor = function () {
            // The ui-ace option
            $scope.aceOption = {
                mode: $scope.fmt,
                onLoad: function (_ace) {

                    // HACK to have the ace instance in the scope...
                    $scope.modeChanged = function () {
                        _ace.getSession().setMode("ace/mode/Scheme");
                    };
                }
            };

            $scope.model = $scope.response.toString();
            $scope.loading = false;
        };

        /**
         * Save new Runtime configuration into File Path.
         */
        $scope.save = function () {
            $scope.loading = true;
            $http({
                method: 'POST',
                url: "api/rule/upload",
                data: $scope.response
            }).success(function (data, status, headers, config) {
                $scope.successMsg = "Successfully saved!";
                $scope.loading = false;
            }).error(function (data, status, headers, config) {
                $scope.errorMsg = data;
                $scope.loading = false;
            });
        };

        $scope.revert = function () {
            $scope.response = $scope.original;
        };

        $scope.setFmt = function (fmt) {
            $scope.fmt = fmt;
            $scope.fetchFile();
        };

        $scope.fetchFile();

    });
