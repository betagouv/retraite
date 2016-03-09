'use strict';

angular.module('SgmapRetraiteConfig').controller('LoginCtrl', function ($scope, UserService, $state) {

    $scope.user = {};

    $scope.login = function() {
        UserService.login($scope.user);
    };

    $scope.test = function() {
        $state.go('alvar-test');
    };

});
