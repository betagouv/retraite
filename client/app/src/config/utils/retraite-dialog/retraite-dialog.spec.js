'use strict';

describe('RetraiteDialog', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    var RetraiteDialog, $timeout, ngDialog, TemplateLoader;

    beforeEach(inject(function (_RetraiteDialog_, _$timeout_, _ngDialog_, _TemplateLoader_) {
        RetraiteDialog = _RetraiteDialog_;
        $timeout = _$timeout_;
        ngDialog = _ngDialog_;
        TemplateLoader = _TemplateLoader_;
    }));
    
    var mockDialog = {
        close: function() {}
    };

    var mockScope = {
        validateDialog: function() {},
        cancelDialog: function() {}
    };

    var obj1 = {id: 1, nom: "nom 1"};
    var obj2 = {id: 2, nom: "nom 2"};

    beforeEach(function() {
        
        spyOn(TemplateLoader, 'loadTemplateUrl').and.callFake(function(url) {
            if (url === 'a/b/tempContent.html') {
                return 'contenu';
            }
            if (url.indexOf('retraite-dialog.html') > 0) {
                return 'a {{title}} b {{content}} c';
            }
        });
        spyOn(mockDialog, 'close');

    });

    it('should display dialog and return result if validated', function (done) {

        spyOn(ngDialog, 'open').and.callFake(function(options) {
            expect(options.plain).toBeTruthy();
            expect(options.template).toEqual('a Mon titre b contenu c');
            
            options.controller(mockScope);
            expect(mockScope.value).toEqual(obj1);
            // Il faut éditer une copie de l'objet pour ne pas modifier l'original
            expect(mockScope.value).not.toBe(obj1);
            
            $timeout(function() {
                mockScope.value = obj2;
                mockScope.validateDialog();
            });
            return mockDialog;
        });
        
        var options = {
            title: "Mon titre",
            templateUrl: 'a/b/tempContent.html',
            value: obj1,
            canValidate: function() {}
        };
        spyOn(options, 'canValidate');
        var promise = RetraiteDialog.display(options);
        promise.then(function onSuccess(result) {

            expect(ngDialog.open).toHaveBeenCalled();
            expect(options.canValidate).toHaveBeenCalledWith(obj2);
            expect(mockDialog.close).toHaveBeenCalled();
            expect(result).toEqual(obj2);
            
            done();
        }, function onError(error) {
            jasmine.fail("Ne devrait pas venir ici");
            done();
        });

        $timeout.flush();
    });

    it('should display dialog and display error if error while validating', function (done) {

        spyOn(ngDialog, 'open').and.callFake(function(options) {
            options.controller(mockScope);
            
            $timeout(function() {
                mockScope.value = obj2;
                mockScope.validateDialog();

                // La vérification finale ne peut se faire qu'ici car le dialog
                // n'est pas fermé et la promise n'est ni résolue ni rejetée
                
                expect(mockDialog.close).not.toHaveBeenCalled();
                expect(mockScope.error).toEqual("il y a une erreur");
                done();
            });
            return mockDialog;
        });
        
        var options = {
            title: "Mon titre",
            templateUrl: 'a/b/tempContent.html',
            value: obj1,
            canValidate: function() {
                return "il y a une erreur";
            }
        };
        var promise = RetraiteDialog.display(options);
        promise.then(function onSuccess(result) {
            jasmine.fail("Ne devrait pas venir ici");
            done();
        }, function onError(error) {
            jasmine.fail("Ne devrait pas venir ici");
            done();
        });

        $timeout.flush();
    });

    it('should display dialog and reject promise if canceled', function (done) {

        spyOn(ngDialog, 'open').and.callFake(function(options) {
            options.controller(mockScope);
            $timeout(function() {
                mockScope.cancelDialog();
            });
            return mockDialog;
        });
        
        var promise = RetraiteDialog.display({
            title: "Mon titre",
            templateUrl: 'a/b/tempContent.html'
        });
        promise.then(function onSuccess(result) {

            jasmine.fail("Ne devrait pas venir ici");
            done();
        }, function onError() {
            done();
        });

        $timeout.flush();
    });

});
