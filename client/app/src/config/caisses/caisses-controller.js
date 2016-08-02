'use strict';

angular.module('SgmapRetraiteConfig').controller('CaissesCtrl', function ($rootScope, $scope, $state, $stateParams, ApiCaisseFilter, PromptService, WsCaisseDepartement) {

    // Actions
    
    $scope.confirmeDepartementDelete = function(caisseId, departement) {
        PromptService.promptQuestion("Confirmation", "Etes-vous sûr de vouloir supprimer ce département ?").then(function() {
            WsCaisseDepartement.deleteDepartement(caisseId, departement).then(function() {
                $state.reload();
            });
        });
    };
    
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

