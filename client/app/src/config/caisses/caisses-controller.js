'use strict';

angular.module('SgmapRetraiteConfig').controller('CaissesCtrl', function ($rootScope, $scope, $state, $stateParams, ApiCaisseFilter, PromptService, WsCaisseDepartement, RetraiteDialog, CaissesUtils) {

    // Actions
    
    $scope.confirmeDepartementDelete = function(caisse, departement) {
        PromptService.promptQuestion("Confirmation", "Etes-vous sûr de vouloir supprimer ce département ?").then(function() {
            if (caisse.departements.length == 1) {
                PromptService.promptQuestion("Confirmation", "Attention, sans département, cette caisse sera supprimée. Etes-vous sûr de vouloir continuer ?").then(function() {
                    deleteDepartement(caisse.id, departement);
                });
            } else {
                deleteDepartement(caisse.id, departement);
            }
        });
    };
    
    $scope.addDepartement = function(checklistName, caisseId) {
        var promise = callDialogToAddDepartement("Ajouter un département");
        if (promise) {
            promise.then(function(departement) {
                WsCaisseDepartement.addDepartement(checklistName, caisseId, departement).then(function() {
                    $state.reload();
                });
            });
        }
    };
    
    $scope.addCaisse = function(checklistName) {
        var promise = callDialogToAddDepartement("Ajouter une caisse");
        if (promise) {
            promise.then(function(departement) {
                WsCaisseDepartement.addCaisse(checklistName, departement).then(function(result) {
                    $state.reload();
                    PromptService.promptInformation("Information", "La caisse a été créée avec un nom par défaut, vous pouvez la retrouver dans la liste de gauche, probablement en fin de liste. Vous pouvez alors cliquer dessus pour modifier ses informations.");
                });
            });
        }
    };
    
    // Evénements
    
    $rootScope.$on('caisseSaved', function() {
        loadAllCaissesForChecklistName();
    });
    
    // Données
    
    function loadAllCaissesForChecklistName() {
        ApiCaisseFilter.allForChecklistName($scope.name).$promise.then(function(caisses) {
            $scope.caisses = caisses;
            $scope.availableDepartements = CaissesUtils.searchAvailableDepartements($scope.caisses);
        });
    };

    $scope.name = $stateParams.name;
    loadAllCaissesForChecklistName();
    
    // Privé
    
    function callDialogToAddDepartement(dialogTitle) {
        
        var availableDepartements = CaissesUtils.searchAvailableDepartements($scope.caisses);
        
        if (availableDepartements.length == 0) {
            PromptService.promptInformation("Attention", "Désolé, aucun département disponible pour être ajouté. Vous devez d'abord supprimer un département de la caisse à laquelle il est affecté en cliquant sur son numéro.");
            return;
        }
        
        return RetraiteDialog.display({
            title: dialogTitle,
            templateUrl: 'src/config/caisses/dialogs/add-departement/add-departement.html',
            value: availableDepartements[0],
            data: {
                departements: availableDepartements
            }
        });
    };
    
    function deleteDepartement(caisseId, departement) {
        WsCaisseDepartement.deleteDepartement(caisseId, departement).then(function() {
            $state.reload();
        });
    };
});

