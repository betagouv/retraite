'use strict';

angular.module('SgmapRetraiteConfig').controller('LogoutCtrl', function ($scope, UserService, $state) {

    $scope.user = {};

    $scope.logout = function() {
        UserService.logout();
    };

});
