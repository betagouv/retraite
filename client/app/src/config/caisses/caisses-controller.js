'use strict';

angular.module('SgmapRetraiteConfig').controller('CaissesCtrl', function ($scope, $stateParams, ApiCaisseDepartementale) {

    // Actions
    
    // Données

    ApiCaisseDepartementale.all($stateParams.name).$promise.then(function(caisses) {
        $scope.caisses = caisses;
    });
    
});

