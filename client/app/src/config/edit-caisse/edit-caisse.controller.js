'use strict';

angular.module('SgmapRetraiteConfig').controller('EditCaisseCtrl', function ($rootScope, $scope, $stateParams, ApiCaisse, $document) {

    // Actions
    
    $scope.save = function() {
        ApiCaisse.save($scope.caisse);
        $rootScope.$broadcast('caisseSaved');
    };
    
    // Données

    ApiCaisse.get($stateParams.id).$promise.then(function(caisse) {
        $scope.caisse = caisse;
    });
    
});

