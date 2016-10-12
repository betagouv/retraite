'use strict';

angular.module('SgmapRetraiteConfig').controller('ConfigListCtrl', function ($scope, $stateParams, CheckList, $state) {
    
    $scope.checklists = CheckList.all();

});

