'use strict';

describe('RetraiteToaster', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var RetraiteToaster, toaster;
    
    beforeEach(inject(function (_RetraiteToaster_, _toaster_) {
        RetraiteToaster = _RetraiteToaster_;
        toaster = _toaster_;
    }));

    describe('popSuccess', function () {
        
        it('should pop toaster for success', function () {

            spyOn(toaster, 'pop');

            RetraiteToaster.popSuccess("Données enregistrées !");

            expect(toaster.pop).toHaveBeenCalledWith({
                type: "success",
                title: "Données enregistrées !",
                showCloseButton: true,
                toasterId: "success"
            });

        });
        
    });

    describe('popError', function () {
        
        it('should pop toaster for error', function () {

            spyOn(toaster, 'pop');

            RetraiteToaster.popError("Erreur lors de l'enregistrement des données !");

            expect(toaster.pop).toHaveBeenCalledWith({
                type: "error",
                title: "Erreur lors de l'enregistrement des données !",
                body: "",
                showCloseButton: true,
                toasterId: "error"
            });

        });

        it('should pop toaster for error with body', function () {

            spyOn(toaster, 'pop');

            RetraiteToaster.popError("Erreur lors de l'enregistrement des données !", "mon body");

            expect(toaster.pop).toHaveBeenCalledWith({
                type: "error",
                title: "Erreur lors de l'enregistrement des données !",
                body: "mon body",
                showCloseButton: true,
                toasterId: "error"
            });

        });

    });
    
    describe('popSuccess', function () {
        
        it('should pop toaster for success', function () {

            spyOn(toaster, 'pop');

            RetraiteToaster.popErrorWithTimeout("Une erreur !");

            expect(toaster.pop).toHaveBeenCalledWith({
                type: "error",
                title: "Une erreur !",
                body: "",
                showCloseButton: true,
                toasterId: "errorTimedout"
            });

        });
        
    });
    
});

