'use strict';

angular.module('SgmapRetraiteConfig').controller('EditCtrl', 
            function ($scope, $state, $stateParams, CheckList, RetraiteDialog, ArrayUtils, EditConditionValidator, $window, PromptService, DialogDelai, RetraiteToaster, ChecklistPublisher, PastedTextCleaner) {

    // Actions
    
    $scope.save = function() {
        CheckList.save($scope.checklist).then(function(checklist) {
            storeChecklist(checklist, false);
            RetraiteToaster.popSuccess("Enregistrée avec succès !");
        });
    };
    
    $scope.publish = function() {
        PromptService.promptQuestion("Confirmation", "Etes-vous sûr de vouloir publier cette checklist ?").then(function() {
            CheckList.save($scope.checklist).then(function(checklist) {
                storeChecklist(checklist, false);
                ChecklistPublisher.publish($scope.checklist.id).then(function() {
                    RetraiteToaster.popSuccess("Enregistrée et publiée avec succès !");
                    getChecklistToEdit();
                });
            });
        });
    };
    
    $scope.openDocumentation = function(published) {
        $window.open('/application/generateDoc?checklistNom='+$scope.checklist.nom+'&published='+published, '_blank');
    };
    
    $scope.openAllChapitres = function() {
        $scope.checklist.chapitres.forEach(function (chapitre) {
            chapitre.closedInEdition = false;
        });
    };
    
    $scope.closeAllChapitres = function() {
        $scope.checklist.chapitres.forEach(function (chapitre) {
            chapitre.closedInEdition = true;
        });
    };
    
    $scope.addChapitre = function() {
        $scope.checklist.chapitres.push({ titre: "Nouveau chapitre" });
    };
    
    $scope.orderUp = function(chapitre) {
        var chapitres = $scope.checklist.chapitres;
        for(var i = 0; i < chapitres.length; i++) {
            if (chapitres[i] == chapitre) {
                chapitres.splice(i, 1);
                chapitres.splice(i-1, 0, chapitre);
                return;
            }
        }
    };

    $scope.orderDown = function(chapitre) {
        var chapitres = $scope.checklist.chapitres;
        for(var i = 0; i < chapitres.length; i++) {
            if (chapitres[i] == chapitre) {
                chapitres.splice(i, 1);
                chapitres.splice(i+1, 0, chapitre);
                return;
            }
        }
    };
    
    $scope.duplicateChapitre = function(chapitre) {
        var chapitres = $scope.checklist.chapitres;
        for(var i = 0; i < chapitres.length; i++) {
            if (chapitres[i] == chapitre) {
                var newChapitre = angular.copy(chapitre);
                delete newChapitre.id;
                newChapitre.titre += " - Copie";
                chapitres.splice(i+1, 0, newChapitre);
                return;
            }
        }
    };

    $scope.removeChapitre = function(chapitre) {
        
        PromptService.promptQuestion("Confirmation", "Etes-vous sûr de vouloir supprimer ce chapitre ?").then(function() {
            var chapitres = $scope.checklist.chapitres;
            for(var i = 0; i < chapitres.length; i++) {
                if (chapitres[i] == chapitre) {
                    chapitres.splice(i, 1);
                    return;
                }
            }
        });

    };

    $scope.editCondition = function(chapitre, condition) {
        if (condition.props && condition.props.type == 'delai') {
            editConditionDelai(chapitre, condition, false);
        }
    };
    
    // Ajout conditions : type simple
    
    $scope.addConditionType = function(chapitre, type) {
        var condition = {
            props: {
                type: type
            }
        };
        chapitre.conditions = chapitre.conditions || [];
        chapitre.conditions.push(condition);
    };
    
    // Ajout conditions : délai
    
    $scope.addConditionDelai = function(chapitre) {
        editConditionDelai(chapitre, {props:{}}, true);
    };
    
    var editConditionDelai = function(chapitre, condition, adding) {
        var options = {
            title: "Critère : délai",
            templateUrl: 'src/config/edit/dialogs/editConditionDelai.html',
            value: condition,
            canValidate: function(condition) {
                return EditConditionValidator.isDelaiValide(condition);
            }
        };
        RetraiteDialog.display(options).then(function (conditionEdited) {
            conditionEdited.props.type = 'delai';
            chapitre.conditions = chapitre.conditions || [];
            if (adding) {
                chapitre.conditions.push(conditionEdited);
            } else {
                ArrayUtils.replace(chapitre.conditions, condition, conditionEdited);
            }
        });
    };
    
    // Ajout conditions : régime xxx détecté
    
    $scope.addConditionRegimeDetecte = function(chapitre, regime) {
        var conditionRegimeDetecte = {
            props: {
                type:'regimeDetecte',
                regime: regime
            }
        };
        chapitre.conditions = chapitre.conditions || [];
        chapitre.conditions.push(conditionRegimeDetecte);
    };
    
    // Ajout conditions : Statut
    
    $scope.addConditionStatut = function(chapitre, statut) {
        var conditionNsa = {
            props: {
                type:"statut",
                statut: statut
            }
        };
        chapitre.conditions = chapitre.conditions || [];
        chapitre.conditions.push(conditionNsa);
    };
    
    // Suppression d'une condition
    
    $scope.removeCondition = function(chapitre, condition) {
        PromptService.promptQuestion("Confirmation","Etes-vous sûr de vouloir supprimer cette condition ?").then(function() {
            var index = chapitre.conditions.indexOf(condition);
            chapitre.conditions.splice(index, 1);
        });
    };
    
    // Edition des delais
    
    $scope.editDelai = function(chapitre) {
        DialogDelai.displayAndProcess(chapitre);
    };
    
    $scope.editDelaiSA = function(chapitre) {
        DialogDelai.displayAndProcess(chapitre, "SA");
    };
    
    // Conversions
    
    $scope.conditionToHumanStr = function(condition) {
        if (condition.props.type === 'delai') {
            return "Délai "
                +(condition.props.plusOuMoins === 'MOINS' ? '< ' : '> ')
                +condition.props.nombre+' '
                +(condition.props.unite === 'MOIS' ? 'mois' : 'années');
        }
        if (condition.props.type === 'regimeDetecte') {
            if (condition.props.regime === 'agirc-arrco') {
                return "Détecté : AGIRC-ARRCO";
            }
            if (condition.props.regime === 'regimes-base-hors-alignés') {
                return "Détecté : Régimes de base hors alignés";
            }
            if (condition.props.regime === 'regimes-complémentaires-hors-agirc-arrco') {
                return "Détecté : Régimes compl. hors AGIRC-ARRCO";
            }
            if (condition.props.regime === 'regimes-hors-alignés-et-hors-agirc-arrco') {
                return "Détecté : Régimes hors alignés et hors AGIRC-ARRCO";
            }
            return "!! "+JSON.stringify(condition.props).replace(/\"/g, "'")+" !!";
        }
        if (condition.props.type === 'statut') {
            if (condition.props.statut === 'nsa') {
                return "NSA";
            }
            if (condition.props.statut === 'sa') {
                return "SA";
            }
            if (condition.props.statut === 'conjoint-collaborateur') {
                return "Coinjoint-collaborateur";
            }
            if (condition.props.statut === 'chef-entreprise') {
                return "Chef d'entreprise";
            }
            return "!! "+JSON.stringify(condition.props).replace(/\"/g, "'")+" !!";
        }
        if (condition.props.type === 'carriere-longue-oui') {
            return "Carrière longue : Oui (l'assuré a une attestation)";
        }
        if (condition.props.type === 'carriere-longue-non') {
            return "Carrière longue : Non";
        }
        return "!! "+JSON.stringify(condition.props).replace(/\"/g, "'")+" !!";
    };
    
    $scope.delaiToHumanStr = function(delai) {
        if (delai.type === 'AUCUN') {
            return "Aucun";
        }
        if (delai.type === 'DESQUE') {
            return "Dès que possible";
        }
        if (delai.type === 'AUPLUS') {
            return "Au plus tard "+delai.min+" "+(delai.unite === 'MOIS' ? "mois" : (delai.min === 1 ? "année" : "années"));
        }
        if (delai.type === 'ENTRE') {
            return "Entre "+delai.min+" et "+delai.max+" "+(delai.unite === 'MOIS' ? "mois" : "années");
        }
        if (delai.type === 'APARTIR') {
            return "A partir de "+delai.min+" "+(delai.unite === 'MOIS' ? "mois" : (delai.min === 1 ? "année" : "années"));
        }
        if (delai.type === 'SIMPLE') {
            return "A "+delai.min+" "+(delai.unite === 'MOIS' ? "mois" : (delai.min === 1 ? "année" : "années"));
        }
        return "Inconnu : "+JSON.stringify(delai);
    };
    
    $scope.isEditable = function(condition) {
        return (condition.props.type === 'delai');
    };
    
    // Gestion des (copiés-)-collés
    
    $scope.textAreaSetup = function($elt) {
        $elt.on('paste', function(e) {
            var $this = $(this);
            var oldText = $this.html();
            setTimeout(function () {
                var newText = $this.html();
                $this.html(PastedTextCleaner.clean(oldText, newText));
            }, 500);
        });
    };

    // Gestion de l'enregistrement et de la sortie de l'écran
    
    var msgModifiedData = "ATTENTION : les données ont été modifiées. Êtes-vous sûr de vouloir quitter la page ? Les données modifiées seront perdues ! ...";
    
    // . Audition sur les navigations "générales" (navigateur)
    $window.onbeforeunload = function(e) {
        if ($scope.modified) {
            var e = e || window.event;
            // For IE and Firefox
            if (e) {
                e.returnValue = msgModifiedData;
            }
            return msgModifiedData;
        }
    };
    
    // . Audition sur les navigations "internes" ($stateProvider)
    $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        if ($scope.modified) {
            // Par défaut, on désactive la navigation et on pose la question (asynchrone)
            event.preventDefault();
            PromptService.promptQuestion("Confirmation", msgModifiedData).then(function() {
                $scope.modified = false;
                $state.go(toState, toParams);
            });
        }
    });
    
    // Données

    var fnRemoveWatch;
    
    function addWatch() {
        $scope.modified = false;
        fnRemoveWatch = $scope.$watch('checklist', function(newValue, oldValue) {
            console.log("watch : ",oldValue," --> ",newValue);
            console.log(JSON.stringify(oldValue));
            console.log(JSON.stringify(newValue));
            if (!ObjectUtils.equalsIgnoringProperties(newValue, oldValue, ['closedInEdition'])) {
                $scope.modified = true;
            }
        }, true);
    }
    
    function storeChecklist(checklist) {
        if (fnRemoveWatch) {
            fnRemoveWatch();
        }
        $scope.checklist = checklist;
        addWatch();
    }
    
    function getChecklistToEdit() {
        var checklist = CheckList.get($stateParams.clid);
        checklist.$promise.then(function() {
            storeChecklist(checklist);
        });
    }
    getChecklistToEdit();
    
});

