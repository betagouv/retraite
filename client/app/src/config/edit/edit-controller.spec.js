'use strict';

describe('EditCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var $scope, controller, CheckList, RetraiteDialog, EditConditionValidator, $window, PromptService, DialogDelai, RetraiteToaster, ChecklistPublisher, SyntaxAnalyzer;
    
    beforeEach(inject(function ($rootScope, $controller, _CheckList_, _RetraiteDialog_, _EditConditionValidator_, _$window_, _PromptService_, _DialogDelai_, _RetraiteToaster_, _ChecklistPublisher_, _SyntaxAnalyzer_) {

        CheckList = _CheckList_;
        RetraiteDialog = _RetraiteDialog_;
        EditConditionValidator = _EditConditionValidator_;
        $window = _$window_;
        PromptService = _PromptService_;
        DialogDelai = _DialogDelai_;
        RetraiteToaster = _RetraiteToaster_;
        ChecklistPublisher = _ChecklistPublisher_;
        SyntaxAnalyzer = _SyntaxAnalyzer_;
        
    }));
    
    var mockCheckList1 = {
        id:2,
        nom:"nom 1",
        $promise: {
            then: function(callback) {
                callback(mockCheckList1);
            }
        }
    };
    var mockCheckList1b = {
        id:2,
        nom:"nom 1b",
        $promise: {
            then: function(callback) {
                callback(mockCheckList1b);
            }
        }
    };
    var mockCheckList2 = {
        id:2,
        nom:"nom 2"
    };

    beforeEach(function() {
        spyOn(CheckList, 'get').and.returnValue(mockCheckList1);
        spyOn(CheckList, 'save').and.returnValue({
            then: function(callback) {
                callback(mockCheckList2);
            }
        });
        spyOn(EditConditionValidator, 'isDelaiValide').and.returnValue("Erreur de validation");
        spyOn(RetraiteToaster, 'popSuccess');
        spyOn(ChecklistPublisher, 'publish').and.returnValue({
            then: function(success) {
                success();
            }
        });
    });

    beforeEach(inject(function ($rootScope, $controller, _CheckList_) {

        $scope = $rootScope.$new();
        controller = $controller('EditCtrl', {
            $scope: $scope            
        });

    }));
    
    it('should have init data in scope', function () {
        expect($scope.checklist).toEqual(mockCheckList1);
    });

    describe('save', function () {
        
        it('should save the edited checklist', function () {
            
            $scope.save();
            
            expect($scope.checklist).toEqual(mockCheckList2);
            expect(RetraiteToaster.popSuccess).toHaveBeenCalledWith("Enregistrée avec succès !");
        });
        
    });

    describe('publish', function () {
        
        it('should ask for confirm and do nothing if not confirmed', function () {
            
            spyOn(PromptService, 'promptQuestion').and.returnValue({
                then: function(callback) {
                    // Do nothing to not confir
                }
            });

            $scope.publish();

            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr'));
            expect(CheckList.save).not.toHaveBeenCalled();
            expect(ChecklistPublisher.publish).not.toHaveBeenCalled();
            expect(RetraiteToaster.popSuccess).not.toHaveBeenCalled();
        });
        
        it('should save and publish if confirmed', function () {
            
            spyOn(PromptService, 'promptQuestion').and.returnValue({
                then: function(callback) {
                    // Confirm !
                    callback();
                }
            });
            // Override return for this test
            CheckList.get.and.returnValue(mockCheckList1b);

            $scope.publish();

            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr'));
            expect(CheckList.save).toHaveBeenCalledWith(mockCheckList1);
            expect(ChecklistPublisher.publish).toHaveBeenCalledWith(2);
            expect(RetraiteToaster.popSuccess).toHaveBeenCalledWith("Enregistrée et publiée avec succès !");
            expect($scope.checklist).toEqual(mockCheckList1b);
        });
        
    });

    describe('openDocumentation', function () {
        
        it('should open documentation in new tab', function () {
            
            spyOn($window, 'open');
            
            $scope.openDocumentation(true);
            
            expect($window.open).toHaveBeenCalledWith('/application/generateDoc?checklistNom=nom 1&published=true', '_blank');
            
        });
        
    });

    describe('for chapitre array', function () {
        
        var chapitre6, chapitre7, chapitre8, chapitre9;
        var chapitres;
        
        beforeEach(function() {
            chapitre6 = { id: 666, titre: "Chap 6", closedInEdition: false };
            chapitre7 = { 
                id: 777, 
                titre: "Chap 7", 
                conditions: [{
                    props: {
                        type: "carriere-longue-oui"
                    },
                    id: 748
                }],
                closedInEdition: true 
            };
            chapitre8 = { id: 888, titre: "Chap 8", closedInEdition: false };
            chapitre9 = { id: 999, titre: "Chap 9", closedInEdition: true };
            chapitres = $scope.checklist.chapitres = [
                chapitre6, chapitre7, chapitre8, chapitre9
            ];
        });

        describe('openAllChapitres', function () {

            it('should order up a sous-chapitre', function () {

                $scope.openAllChapitres(chapitre8);

                expect(chapitres[0].closedInEdition).toBeFalsy();
                expect(chapitres[1].closedInEdition).toBeFalsy();
                expect(chapitres[2].closedInEdition).toBeFalsy();
                expect(chapitres[3].closedInEdition).toBeFalsy();
            });

        });

        describe('closeAllChapitres', function () {

            it('should order up a sous-chapitre', function () {

                $scope.closeAllChapitres(chapitre8);

                expect(chapitres[0].closedInEdition).toBeTruthy();
                expect(chapitres[1].closedInEdition).toBeTruthy();
                expect(chapitres[2].closedInEdition).toBeTruthy();
                expect(chapitres[3].closedInEdition).toBeTruthy();
            });

        });

        describe('addChapitre', function () {

            it('should add a new chapitre', function () {

                $scope.checklist.chapitres = [
                    {
                        titre: "Chapitre 1"
                    },{
                        titre: "Chapitre 2"
                    }
                ];

                $scope.addChapitre();

                var expectedChapitres = [
                    {
                        titre: "Chapitre 1"
                    },{
                        titre: "Chapitre 2"
                    },{
                        titre: "Nouveau chapitre"
                    }
                ];

            });

        });

        describe('orderUp', function () {

            it('should order up a sous-chapitre', function () {

                $scope.orderUp(chapitre8);

                expect(chapitres.length).toEqual(4);
                expect(chapitres[0].id).toEqual(666);
                expect(chapitres[1].id).toEqual(888);
                expect(chapitres[2].id).toEqual(777);
                expect(chapitres[3].id).toEqual(999);
            });

        });

        describe('orderDown', function () {

            it('should order down a chapitre', function () {

                $scope.orderDown(chapitre7);

                expect(chapitres.length).toEqual(4);
                expect(chapitres[0].id).toEqual(666);
                expect(chapitres[1].id).toEqual(888);
                expect(chapitres[2].id).toEqual(777);
                expect(chapitres[3].id).toEqual(999);
            });

        });

        describe('duplicateChapitre', function () {

            it('should duplicate a chapitre', function () {

                $scope.duplicateChapitre(chapitre7);

                expect(chapitres.length).toEqual(5);
                expect(chapitres[0].id).toEqual(666);
                expect(chapitres[1].id).toEqual(777);
                expect(chapitres[2].id).not.toBeDefined();
                expect(chapitres[2].titre).toEqual("Chap 7 - Copie");
                expect(chapitres[2].conditions[0].props.type).toEqual("carriere-longue-oui");
                expect(chapitres[2].conditions[0].id).not.toBeDefined();
                expect(chapitres[3].id).toEqual(888);
                expect(chapitres[4].id).toEqual(999);
            });

        });

        describe('removeChapitre', function () {

            it('should ask for confirmation and remove chapitre', function () {

                spyOn(PromptService, 'promptQuestion').and.returnValue({
                    then: function(callback) {
                        callback();
                    }
                });

                $scope.removeChapitre(chapitre7);

                expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr'));
                expect(chapitres.length).toEqual(3);
                expect(chapitres[0].id).toEqual(666);
                expect(chapitres[1].id).toEqual(888);
                expect(chapitres[2].id).toEqual(999);
            });

            it('should ask for confirmation and not remove chapitre if not confirmed', function () {

                spyOn(PromptService, 'promptQuestion').and.returnValue({
                    then: function(callback) {
                    }
                });

                $scope.removeChapitre(chapitre7);

                expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr'));
                expect(chapitres.length).toEqual(4);
                expect(chapitres[0].id).toEqual(666);
                expect(chapitres[1].id).toEqual(777);
                expect(chapitres[2].id).toEqual(888);
                expect(chapitres[3].id).toEqual(999);
            });

        });
    
    });

    describe('addConditionType', function () {

        var conditionExpected = {
            props: {
                type: 'type-toto'
            }
        };
        
        var chapitre = {};
        
        it('should add condition to chapitre', function () {
            
            $scope.addConditionType(chapitre, 'type-toto');
            
            expect(chapitre.conditions).toEqual([ conditionExpected ] );
        });
        
    });

    describe('addConditionDelai', function () {

        var conditionDelaiFromDialog = {
            props: {
                plusOuMoins:"MOINS",
                nombre:"3",
                unite:"MOIS"
            }
        };
        var conditionDelaiStored = {
            props: {
                type:"delai",
                plusOuMoins:"MOINS",
                nombre:"3",
                unite:"MOIS"
            }
        };
        
        var chapitre;
        
        beforeEach(function() {
            
            chapitre = {};

            spyOn(RetraiteDialog, 'display').and.callFake(function(options) {
                expect(options.title).toEqual("Critère : délai");
                expect(options.templateUrl).toEqual('src/config/edit/dialogs/editConditionDelai.html');
                expect(options.value).toEqual({props:{}});
                expect(typeof options.canValidate).toEqual('function');
                var errorValidating = options.canValidate({});
                expect(errorValidating).toEqual("Erreur de validation");
                return {
                    then: function(callback) {
                        callback(conditionDelaiFromDialog);
                    }
                };
            });
            
        });

        it('should open dialog and add condition in chapitre', function () {
            
            $scope.addConditionDelai(chapitre);
            
            expect(RetraiteDialog.display).toHaveBeenCalled();
            expect(EditConditionValidator.isDelaiValide).toHaveBeenCalled();
            expect(chapitre.conditions).toEqual( [ conditionDelaiStored ] );
        });
        
    });

    describe('editCondition', function () {

        var conditionDelaiBefore = {
            props: {
                type:"delai",
                plusOuMoins:"MOINS",
                nombre:"3",
                unite:"MOIS"
            }
        };
        
        var conditionDelaiAfter = {
            props: {
                type:"delai",
                plusOuMoins:"PLUS",
                nombre:"4",
                unite:"ANNEES"
            }
        };
        
        var chapitre = {
            conditions: [
                "une condition",
                conditionDelaiBefore,
                "une autre condition"
            ]
        };
        
        beforeEach(function() {
            
            spyOn(RetraiteDialog, 'display').and.callFake(function(options) {
                expect(options.title).toEqual("Critère : délai");
                expect(options.templateUrl).toEqual('src/config/edit/dialogs/editConditionDelai.html');
                expect(options.value).toEqual(conditionDelaiBefore);
                expect(typeof options.canValidate).toEqual('function');
                return {
                    then: function(callback) {
                        callback(conditionDelaiAfter);
                    }
                };
            });
            
        });

        it('should open dialog and add condition in chapitre', function () {
            
            $scope.editCondition(chapitre, conditionDelaiBefore);
            
            expect(RetraiteDialog.display).toHaveBeenCalled();
            var expectedConditions = [
                "une condition",
                conditionDelaiAfter,
                "une autre condition"
            ];
            expect(angular.equals(chapitre.conditions, expectedConditions)).toBeTruthy();
        });
        
    });

    describe('addConditionRegimeDetecte', function () {

        var conditionExpected = {
            props: {
                type: 'regimeDetecte',
                regime: 'agirc-arrco'
            }
        };
        
        var chapitre = {};
        
        it('should add condition to chapitre', function () {
            
            $scope.addConditionRegimeDetecte(chapitre, 'agirc-arrco');
            
            expect(chapitre.conditions).toEqual([ conditionExpected ] );
        });
        
    });

    describe('addConditionStatut', function () {

        var conditionExpected = {
            props: {
                type:'statut', statut: 'nsa'
            }
        };
        
        var chapitre = {};
        
        it('should add condition to chapitre', function () {
            
            $scope.addConditionStatut(chapitre, 'nsa');
            
            expect(chapitre.conditions).toEqual([ conditionExpected ] );
        });
        
    });

    describe('removeCondition', function () {

        it('should ask for confirmation and remove condition from chapitre', function () {
            
            spyOn(PromptService, 'promptQuestion').and.returnValue({
                then: function(callback) {
                    // Confirm !
                    callback();
                }
            });
            
            var condition1 = {id:1, props:{type: "a"}};
            var condition2 = {id:2, props:{type: "b"}};
            var condition3 = {id:3, props:{type: "c"}};
            var chapitre = {
                conditions: [ condition1, condition2, condition3 ]
            };

            $scope.removeCondition(chapitre, condition2);
            
            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr'));
            expect(chapitre.conditions).toEqual([ condition1, condition3 ] );
        });
        
        it('should ask for confirmation and not remove condition from chapitre if not confirmed', function () {
            
            spyOn(PromptService, 'promptQuestion').and.returnValue({
                then: function(callback) {
                }
            });
            
            var condition1 = {id:1, props:{type: "a"}};
            var condition2 = {id:2, props:{type: "b"}};
            var condition3 = {id:3, props:{type: "c"}};
            var chapitre = {
                conditions: [ condition1, condition2, condition3 ]
            };

            $scope.removeCondition(chapitre, condition2);
            
            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr'));
            expect(chapitre.conditions).toEqual([ condition1, condition2, condition3 ] );
        });
        
    });

    describe('conditionToHumanStr', function () {

        describe('for unexpected type', function () {

            it('should throw error', function () {

                var condition = {
                    props: {
                        type:"xxx"
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("!! {'type':'xxx'} !!");
            });
        
        });
        
        describe('for delai', function () {

            it('should generate human string', function () {

                var condition = {
                    props: {
                        type:"delai",
                        plusOuMoins:"MOINS",
                        nombre:"3",
                        unite:"ANNEES"
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Délai < 3 années");
            });
        
        });
        
        describe('for regime détecté', function () {

            it('unknown', function () {

                var condition = {
                    props: {
                        type: 'regimeDetecte',
                        regime: 'xxx'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("!! {'type':'regimeDetecte','regime':'xxx'} !!");
            });
        
            it('agirc-arrco', function () {

                var condition = {
                    props: {
                        type: 'regimeDetecte',
                        regime: 'agirc-arrco'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Détecté : AGIRC-ARRCO");
            });
        
            it('regimes-base-hors-alignés', function () {

                var condition = {
                    props: {
                        type: 'regimeDetecte',
                        regime: 'regimes-base-hors-alignés'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Détecté : Régimes de base hors alignés");
            });
        
            it('regimes-complémentaires-hors-agirc-arrco', function () {

                var condition = {
                    props: {
                        type: 'regimeDetecte',
                        regime: 'regimes-complémentaires-hors-agirc-arrco'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Détecté : Régimes compl. hors AGIRC-ARRCO");
            });
        
            it('regimes-hors-alignés-et-hors-agirc-arrco', function () {

                var condition = {
                    props: {
                        type: 'regimeDetecte',
                        regime: 'regimes-hors-alignés-et-hors-agirc-arrco'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Détecté : Régimes hors alignés et hors AGIRC-ARRCO");
            });
        
        });
        
        describe('for statut', function () {

            it('unknown', function () {

                var condition = {
                    props: {
                        type:'statut', statut: 'xxx'
                    }
                };
                var callFct = function() {
                    $scope.conditionToHumanStr(condition);
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("!! {'type':'statut','statut':'xxx'} !!");
            });
        
            it('nsa', function () {

                var condition = {
                    props: {
                        type:'statut', statut: 'nsa'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("NSA");
            });
        
            it('nsa', function () {

                var condition = {
                    props: {
                        type:'statut', statut: 'sa'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("SA");
            });
        
        });
        
        describe('for statut conjoint-collaborateur', function () {

            it('should generate human string', function () {

                var condition = {
                    props: {
                        type:'statut', statut: 'conjoint-collaborateur'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Coinjoint-collaborateur");
            });
        
        });
        
        describe('for statut chef-entreprise', function () {

            it('should generate human string', function () {

                var condition = {
                    props: {
                        type:'statut', statut: 'chef-entreprise'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Chef d'entreprise");
            });
        
        });
        
        describe('for carriere-longue-oui', function () {

            it('should generate human string', function () {

                var condition = {
                    props: {
                        type:'carriere-longue-oui'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Carrière longue : Oui (l'assuré a une attestation)");
            });
        
        });
        
        describe('for carriere-longue-non', function () {

            it('should generate human string', function () {

                var condition = {
                    props: {
                        type:'carriere-longue-non'
                    }
                };
                var str = $scope.conditionToHumanStr(condition);
                expect(str).toEqual("Carrière longue : Non");
            });
        
        });
        
    });

    describe('delaiToHumanStr', function () {

        describe('AUCUN', function () {

            it('should generate human string', function () {
                var delai = {
                    type: 'AUCUN',
                    min: 1,
                    max: 1,
                    unite: 'MOIS'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("Aucun");
            });
        
        });

        describe('DESQUE', function () {

            it('should generate human string', function () {
                var delai = {
                    type: 'DESQUE',
                    min: 1,
                    max: 1,
                    unite: 'MOIS'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("Dès que possible");
            });
        
        });

        describe('AUPLUS', function () {
            
            it('should generate human string with mois', function () {
                var delai = {
                    type: 'AUPLUS',
                    min: 3,
                    max: 1,
                    unite: 'MOIS'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("Au plus tard 3 mois");
            });
        
            it('should generate human string with années', function () {
                var delai = {
                    type: 'AUPLUS',
                    min: 7,
                    max: 1,
                    unite: 'ANNEES'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("Au plus tard 7 années");
            });
        
            it('should generate human string with année', function () {
                var delai = {
                    type: 'AUPLUS',
                    min: 1,
                    max: 1,
                    unite: 'ANNEES'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("Au plus tard 1 année");
            });
        
        });
        
        describe('ENTRE', function () {
            
            it('should generate human string with mois', function () {
                var delai = {
                    type: 'ENTRE',
                    min: 3,
                    max: 4,
                    unite: 'MOIS'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("Entre 3 et 4 mois");
            });
        
            it('should generate human string with années', function () {
                var delai = {
                    type: 'ENTRE',
                    min: 1,
                    max: 2,
                    unite: 'ANNEES'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("Entre 1 et 2 années");
            });
        
        });
        
        describe('APARTIR', function () {
            
            it('should generate human string with mois', function () {
                var delai = {
                    type: 'APARTIR',
                    min: 3,
                    max: 1,
                    unite: 'MOIS'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("A partir de 3 mois");
            });
        
            it('should generate human string with années', function () {
                var delai = {
                    type: 'APARTIR',
                    min: 7,
                    max: 1,
                    unite: 'ANNEES'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("A partir de 7 années");
            });
        
            it('should generate human string with année', function () {
                var delai = {
                    type: 'APARTIR',
                    min: 1,
                    max: 1,
                    unite: 'ANNEES'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("A partir de 1 année");
            });
        
        });
        
        describe('APARTIR', function () {
            
            it('should generate human string with mois', function () {
                var delai = {
                    type: 'SIMPLE',
                    min: 3,
                    //max: 1,
                    unite: 'MOIS'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("A 3 mois");
            });
        
            it('should generate human string with année', function () {
                var delai = {
                    type: 'SIMPLE',
                    min: 4,
                    max: 1,
                    unite: 'ANNEES'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("A 4 années");
            });
        
            it('should generate human string with année', function () {
                var delai = {
                    type: 'SIMPLE',
                    min: 1,
                    max: 1,
                    unite: 'ANNEES'
                };
                var str = $scope.delaiToHumanStr(delai);
                expect(str).toEqual("A 1 année");
            });
        
        });
        
    });

    describe('isEditable', function () {
        
        it('should return true if editable', function () {
            
            var condition_delai = { props: { type: 'delai' } };
            expect($scope.isEditable(condition_delai)).toBeTruthy();
            
            var condition_regimeAgircArrcoDetecte = { props: { type: 'regimeAgircArrcoDetecte' } };
            expect($scope.isEditable(condition_regimeAgircArrcoDetecte)).toBeFalsy();
            
            var condition_nsa = { props: { type: 'nsa' } };
            expect($scope.isEditable(condition_nsa)).toBeFalsy();
            
            var condition_sa = { props: { type: 'sa' } };
            expect($scope.isEditable(condition_sa)).toBeFalsy();
            
            var condition_other = { props: { type: 'xxx' } };
            expect($scope.isEditable(condition_other)).toBeFalsy();
            
        });
        
        
    });
    
    describe('editDelai', function () {
        
        var chapitre = {};

        it('should display delai dialog', function () {
            
            spyOn(DialogDelai, 'displayAndProcess');
            
            $scope.editDelai(chapitre);
            
            expect(DialogDelai.displayAndProcess).toHaveBeenCalledWith(chapitre);
        });
        
        
    });
    
    describe('editDelaiSA', function () {
        
        var chapitre = {};

        it('should display delai dialog', function () {
            
            spyOn(DialogDelai, 'displayAndProcess');
            
            $scope.editDelaiSA(chapitre);
            
            expect(DialogDelai.displayAndProcess).toHaveBeenCalledWith(chapitre, "SA");
        });
        
        
    });
    
    describe('isSyntaxError', function () {
        
        it('should return false if SyntaxAnalyzer.isSyntaxError return false', function () {

            spyOn(SyntaxAnalyzer, 'isSyntaxError').and.returnValue(false);
            expect($scope.isSyntaxError("yyy")).toBeFalsy();
            expect(SyntaxAnalyzer.isSyntaxError).toHaveBeenCalledWith("yyy");
            
        });
        
        it('should return true if SyntaxAnalyzer.isSyntaxError return true', function () {

            spyOn(SyntaxAnalyzer, 'isSyntaxError').and.returnValue(true);
            expect($scope.isSyntaxError("xxx")).toBeTruthy();
            expect(SyntaxAnalyzer.isSyntaxError).toHaveBeenCalledWith("xxx");
            
        });
        
    });
    
    describe('countSyntaxErrors', function () {
        
        beforeEach(function() {
            var chapitre6 = { id: 666, titre: "Chap 6", closedInEdition: false };
            var chapitre8 = { id: 888, titre: "Chap 8", closedInEdition: false };
            var chapitre9 = { id: 999, titre: "Chap 9", closedInEdition: true };
            $scope.checklist.chapitres = [
                chapitre6, chapitre8, chapitre9
            ];
        });
        
        it('should return 0 if no error', function () {

            spyOn(SyntaxAnalyzer, 'isSyntaxError').and.returnValue(false);
            expect($scope.countSyntaxErrors()).toEqual(0);
            expect(SyntaxAnalyzer.isSyntaxError.calls.count()).toEqual(9);
            
        });
        
        it('should return 0 if no error', function () {

            spyOn(SyntaxAnalyzer, 'isSyntaxError').and.returnValues(false, true, false, true);
            expect($scope.countSyntaxErrors()).toEqual(2);
            expect(SyntaxAnalyzer.isSyntaxError.calls.count()).toEqual(9);
            
        });
        
    });
    
});

