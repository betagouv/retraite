'use strict';

angular.module('SgmapRetraiteConfig').controller('CaissesCtrl', function ($rootScope, $scope, $state, $stateParams, ApiCaisseFilter, PromptService, WsCaisseDepartement, RetraiteDialog, CaissesUtils) {

    // Actions
    
    $scope.confirmeDepartementDelete = function(caisse, departement) {
        PromptService.promptQuestion("Confirmation", "Etes-vous sûr de vouloir supprimer ce département ?").then(function() {
            PromptService.promptQuestion("Confirmation", "Attention, sans département, cette caisse sera supprimée. Etes-vous sûr de vouloir continuer ?").then(function() {
                deleteDepartement(caisse.id, departement);
            });
        });
    };
    
    $scope.addDepartement = function(checklistName, caisseId) {
        
        var availableDepartements = CaissesUtils.searchAvailableDepartements($scope.caisses);
        
        if (availableDepartements.length == 0) {
            PromptService.promptInformation("Attention", "Désolé, aucun département disponible pour être ajouté. Vous devez d'abord supprimer un département de la caisse à laquelle il est affecté en cliquant sur son numéro.");
            return;
        }
        
        RetraiteDialog.display({
            title: "Ajouter un département",
            templateUrl: 'src/config/caisses/dialogs/add-departement/add-departement.html',
            value: availableDepartements[0],
            data: {
                departements: availableDepartements
            }
        }).then(function(departement) {
            WsCaisseDepartement.addDepartement(checklistName, caisseId, departement).then(function() {
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
    
    
    // Privé
    
    function deleteDepartement(caisseId, departement) {
        WsCaisseDepartement.deleteDepartement(caisseId, departement).then(function() {
            $state.reload();
        });
    };
});

