'use strict';

describe('CaissesCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var $rootScope, $scope, controller, ApiCaisseFilter, PromptService, WsCaisseDepartement, $state, RetraiteDialog, CaissesUtils;
    
    beforeEach(inject(function (_ApiCaisseFilter_, _PromptService_, _WsCaisseDepartement_, _$state_, _RetraiteDialog_, _CaissesUtils_) {
        ApiCaisseFilter = _ApiCaisseFilter_;
        PromptService = _PromptService_;
        WsCaisseDepartement = _WsCaisseDepartement_;
        $state = _$state_;
        RetraiteDialog = _RetraiteDialog_;
        CaissesUtils = _CaissesUtils_;
    }));
    
    var mockCaisses = [{
        "nom": "CARSAT Aquitaine",
        "adresse1": "80 avenue de la Jallère",
        "adresse2": "Quartier du Lac",
        "adresse3": "33053 BORDEAUX CEDEX",
        "id": 29
    }, {
        "nom": "CARSAT Auvergne",
        "adresse1": "63036 CLERMONT FERRAND CEDEX 1",
        "adresse2": "",
        "adresse3": "",
        "id": 30
    }];

    beforeEach(function() {
        spyOn(ApiCaisseFilter, 'allForChecklistName').and.callFake(function(name) {
            
            if (name == "CNAV") {
                return {
                    $promise: {
                        then: function(onSuccess, onError) {
                            onSuccess(mockCaisses);
                        }
                    }
                };
            }
            
        });
    });

    beforeEach(inject(function (_$rootScope_, $controller) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        var $stateParams = {
            name: "CNAV"
        };
        
        controller = $controller('CaissesCtrl', {
            $scope: $scope,
            $stateParams: $stateParams
        });

    }));
    
    it('should have init data in scope', function () {
        expect($scope.caisses).toEqual(mockCaisses);
    });

    it('should refresh if event caisseSaved', function () {
        
        $rootScope.$broadcast('caisseSaved');
        
        expect(ApiCaisseFilter.allForChecklistName.calls.count()).toEqual(2);
    });
    
    describe('confirmeDepartementDelete', function() {
        
        beforeEach(function () {
            spyOn(WsCaisseDepartement, 'deleteDepartement').and.returnValue({
                then: function(callback) {
                    callback();
                }
            });
            spyOn($state, 'reload');
        });
        
        it('should ask confirmation and del if confirmed', function () {
            
            spyOn(PromptService, 'promptQuestion').and.returnValue({
                then: function(callback) {
                    // Confirm !
                    callback();
                }
            });

            var caisse = {
                id:29,
                departements: ["05", "14", "74"]
            };
            var departement = "14";
            
            $scope.confirmeDepartementDelete(caisse, departement);

            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr'));
            expect(WsCaisseDepartement.deleteDepartement).toHaveBeenCalledWith(29, "14");
            expect($state.reload).toHaveBeenCalled();
        });
        
        it('should ask confirmation and do nothing if not confirmed', function () {
            
            spyOn(PromptService, 'promptQuestion').and.returnValue({
                then: function(callback) {
                    // Do nothing to not confirm
                }
            });

            var caisse = {
                id:29,
                departements: ["05", "14", "74"]
            };
            var departement = "14";
            
            $scope.confirmeDepartementDelete(caisse, departement);

            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr'));
            expect(PromptService.promptQuestion).not.toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Attention, sans département, cette caisse sera supprimée. Etes-vous sûr de vouloir continuer ?'));
            expect(WsCaisseDepartement.deleteDepartement).not.toHaveBeenCalled();
        });
        
        it('should ask confirmation for Caisse deletion if only one departement attached and del if confirmed', function () {
            
            spyOn(PromptService, 'promptQuestion').and.returnValue({
                then: function(callback) {
                    // Confirm !
                    callback();
                }
            });

            var caisse = {
                id:29,
                departements: ["05"]
            };
            var departement = "14";
            
            $scope.confirmeDepartementDelete(caisse, departement);

            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr de vouloir supprimer ce département ?'));
            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Attention, sans département, cette caisse sera supprimée. Etes-vous sûr de vouloir continuer ?'));
            expect(WsCaisseDepartement.deleteDepartement).toHaveBeenCalledWith(29, "14");
            expect($state.reload).toHaveBeenCalled();
        });
        
        it('should ask confirmation for Caisse deletion if only one departement attached do nothing if not confirmed twice', function () {
            
            var promptQuestionValidated = {
                then: function(callback) {
                    // Confirm !
                    callback();
                }
            };
            var promptQuestionCanceled = {
                then: function(callback) {
                    // Do nothing to not confirm
                }
            };
            spyOn(PromptService, 'promptQuestion').and.returnValues(promptQuestionValidated, promptQuestionCanceled);

            var caisse = {
                id:29,
                departements: ["05"]
            };
            var departement = "14";
            
            $scope.confirmeDepartementDelete(caisse, departement);

            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Etes-vous sûr de vouloir supprimer ce département ?'));
            expect(PromptService.promptQuestion).toHaveBeenCalledWith("Confirmation", jasmine.stringMatching('Attention, sans département, cette caisse sera supprimée. Etes-vous sûr de vouloir continuer ?'));
            expect(WsCaisseDepartement.deleteDepartement).not.toHaveBeenCalledWith(29, "14");
            expect($state.reload).not.toHaveBeenCalled();
        });
        
    });

    describe('addDepartement', function() {
        
        beforeEach(function () {

            spyOn(WsCaisseDepartement, 'addDepartement').and.returnValue({
                then: function(callback) {
                    callback();
                }
            });
            
            spyOn($state, 'reload');
            
        });
        
        it('should display dialog and call WS', function () {
            
            spyOn(CaissesUtils, 'searchAvailableDepartements').and.returnValue(["05", "14", "59"]);
            
            spyOn(RetraiteDialog, 'display').and.callFake(function(options) {
                expect(options.title).toEqual("Ajouter un département");
                expect(options.templateUrl).toEqual('src/config/caisses/dialogs/add-departement/add-departement.html');
                expect(options.value).toEqual("05");
                expect(options.data.departements).toEqual(["05", "14", "59"]);
                return {
                    then: function(callback) {
                        callback("14");
                    }
                };
            });

            var caisseId = 29;

            $scope.addDepartement("CNAV", caisseId);

            expect(CaissesUtils.searchAvailableDepartements).toHaveBeenCalledWith(mockCaisses);
            expect(RetraiteDialog.display).toHaveBeenCalled();
            expect(WsCaisseDepartement.addDepartement).toHaveBeenCalledWith("CNAV", caisseId, "14");
            expect($state.reload).toHaveBeenCalled();
            
        });
        
        it('should display dialog and do nothing if not confirmed', function () {
            
            spyOn(CaissesUtils, 'searchAvailableDepartements').and.returnValue(["05", "14", "59"]);
            
            spyOn(RetraiteDialog, 'display').and.callFake(function(options) {
                return {
                    then: function(callback) {
                        // Pas d'appel à callback = pas de validation
                    }
                };
            });

            var caisseId = 29;

            $scope.addDepartement("CNAV", caisseId);

            expect(CaissesUtils.searchAvailableDepartements).toHaveBeenCalledWith(mockCaisses);
            expect(RetraiteDialog.display).toHaveBeenCalled();
            expect(WsCaisseDepartement.addDepartement).not.toHaveBeenCalled();
            expect($state.reload).not.toHaveBeenCalled();

        });
        
        it('should not display dialog but error message if no departement available', function () {
            
            spyOn(CaissesUtils, 'searchAvailableDepartements').and.returnValue([]);
            
            spyOn(RetraiteDialog, 'display').and.callFake(function(options) {
                return {
                    then: function(callback) {
                        // Pas d'appel à callback = pas de validation
                    }
                };
            });
            spyOn(PromptService, 'promptInformation')

            var caisseId = 29;

            $scope.addDepartement("CNAV", caisseId);

            expect(CaissesUtils.searchAvailableDepartements).toHaveBeenCalledWith(mockCaisses);
            expect(PromptService.promptInformation).toHaveBeenCalledWith("Attention", jasmine.stringMatching("Désolé, aucun département disponible"));
            expect(RetraiteDialog.display).not.toHaveBeenCalled();
            expect(WsCaisseDepartement.addDepartement).not.toHaveBeenCalled();
            expect($state.reload).not.toHaveBeenCalled();

        });
        
    });

    describe('addCaisse', function() {
        
        beforeEach(function () {

            spyOn(WsCaisseDepartement, 'addCaisse').and.returnValue({
                then: function(callback) {
                    callback({caisseId:92});
                }
            });
            
            spyOn($state, 'reload');
            spyOn($state, 'go');
            
        });
        
        it('should display dialog and call WS', function () {
            
            spyOn(CaissesUtils, 'searchAvailableDepartements').and.returnValue(["05", "14", "59"]);

            spyOn(RetraiteDialog, 'display').and.callFake(function(options) {
                expect(options.title).toEqual("Ajouter une caisse");
                expect(options.templateUrl).toEqual('src/config/caisses/dialogs/add-departement/add-departement.html');
                expect(options.value).toEqual("05");
                expect(options.data.departements).toEqual(["05", "14", "59"]);
                return {
                    then: function(callback) {
                        callback("14");
                    }
                };
            });
            
            spyOn(PromptService, 'promptInformation')

            $scope.addCaisse("CNAV");

            expect(RetraiteDialog.display).toHaveBeenCalled();
            expect(WsCaisseDepartement.addCaisse).toHaveBeenCalledWith("CNAV", "14");
            expect($state.reload).toHaveBeenCalled();
            expect($state.go).toHaveBeenCalledWith('caisses.edit', {name:"CNAV", id:92});
            expect(PromptService.promptInformation).toHaveBeenCalledWith("Information", jasmine.stringMatching("La caisse a été créée "));
            
        });
        
    });
    
});

