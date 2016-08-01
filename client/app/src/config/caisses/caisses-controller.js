'use strict';

angular.module('SgmapRetraiteConfig').controller('CaissesCtrl', function ($scope, $stateParams, ApiCaisseFilter) {

    // Actions
    
    // Données

    $scope.name = $stateParams.name;
    ApiCaisseFilter.allForChecklistName($scope.name).$promise.then(function(caisses) {
        $scope.caisses = caisses;
    });
    
});

