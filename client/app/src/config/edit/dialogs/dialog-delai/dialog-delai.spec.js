'use strict';

describe('DialogDelai', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    var DialogDelai, RetraiteDialog;

    beforeEach(inject(function (_DialogDelai_, _RetraiteDialog_) {
        DialogDelai = _DialogDelai_;
        RetraiteDialog = _RetraiteDialog_;
    }));
    
    var chapitre = {};

    describe('check dialog calls ', function () {
        
        beforeEach(function() {
            spyOn(RetraiteDialog, 'display').and.returnValue({
                then: function() {}
            });
        });

        it('should display dialog', function () {

            DialogDelai.displayAndProcess(chapitre);

            expect(RetraiteDialog.display).toHaveBeenCalled();
            var options = RetraiteDialog.display.calls.mostRecent().args[0];
            expect(options.title).toEqual("Configuration du délai");
            expect(options.templateUrl).toEqual('src/config/edit/dialogs/dialog-delai/dialog-delai.html');
        });

        it('should create new empty value if none', function () {

            DialogDelai.displayAndProcess(chapitre);

            var options = RetraiteDialog.display.calls.mostRecent().args[0];
            expect(options.value).toEqual({
                type: 'desque',
                min: 1,
                max: 1,
                unite: 'MOIS'
            });
        });

        it('should call dialog with delai in chapitre', function () {
            
            chapitre.delai = {
                type: 'desque'
            };
            chapitre.delaiSA = {
                type: 'auplus'
            };

            DialogDelai.displayAndProcess(chapitre);

            var options = RetraiteDialog.display.calls.mostRecent().args[0];
            expect(options.value).toBe(chapitre.delai);
        });

        it('should call dialog with delaiSA in chapitre', function () {
            
            chapitre.delai = {
                type: 'desque'
            };
            chapitre.delaiSA = {
                type: 'auplus'
            };

            DialogDelai.displayAndProcess(chapitre, "SA");

            var options = RetraiteDialog.display.calls.mostRecent().args[0];
            expect(options.value).toBe(chapitre.delaiSA);
        });

    });

    describe('check dialog canceled ', function () {
        
        beforeEach(function() {
            spyOn(RetraiteDialog, 'display').and.returnValue({
                then: function(onSuccess, onError) {
                    expect(onSuccess).toEqual(jasmine.any(Function));
                }
            });
        });

        it('should display dialog', function () {

            var delai = {
                type: 'desque'
            };
            chapitre.delai = delai;

            DialogDelai.displayAndProcess(chapitre);

            expect(chapitre.delai).toBe(delai);
        });

    });

    describe('check dialog validated ', function () {
        
        var delaiEdited = {
            type: 'entre'
        };
        
        beforeEach(function() {
            spyOn(RetraiteDialog, 'display').and.returnValue({
                then: function(onSuccess, onError) {
                    expect(onSuccess).toEqual(jasmine.any(Function));
                    onSuccess(delaiEdited);
                }
            });
        });

        it('should display dialog', function () {

            chapitre.delai = {
                type: 'desque'
            };
            chapitre.delaiSA = {
                type: 'auplus'
            };

            DialogDelai.displayAndProcess(chapitre);

            expect(chapitre.delai).toBe(delaiEdited);
        });

        it('should display dialog', function () {

            chapitre.delai = {
                type: 'desque'
            };
            chapitre.delaiSA = {
                type: 'auplus'
            };

            DialogDelai.displayAndProcess(chapitre, "SA");

            expect(chapitre.delaiSA).toBe(delaiEdited);
        });

    });

});
