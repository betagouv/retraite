'use strict';

angular.module('SgmapRetraiteConfig').controller('EditCaisseCtrl', function ($scope, $stateParams, ApiCaisse) {

    // Actions
    
    $scope.save = function() {
        ApiCaisse.save($scope.caisse);
    };
    
    // Données

    ApiCaisse.get($stateParams.id).$promise.then(function(caisse) {
        $scope.caisse = caisse;
    });
    
});

