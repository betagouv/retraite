'use strict';

angular.module('SgmapRetraiteConfig').controller('CaissesCtrl', function ($rootScope, $scope, $stateParams, ApiCaisseFilter) {

    // Actions
    
    // Evénements
    
    $rootScope.$on('caisseSaved', function() {
        load();
    });
    
    // Données
    
    function load() {
        ApiCaisseFilter.allForChecklistName($scope.name).$promise.then(function(caisses) {
            $scope.caisses = caisses;
        });
    };

    $scope.name = $stateParams.name;
    load();
    
});

