'use strict';

angular.module('SgmapRetraiteConfig').controller('ConfigListCtrl', function ($scope, $stateParams, CheckList) {
    
    $scope.checklists = CheckList.all();

});

