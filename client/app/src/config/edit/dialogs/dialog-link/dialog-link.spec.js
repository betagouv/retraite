'use strict';

describe('DialogLink', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    var DialogLink, RetraiteDialog, $timeout;

    beforeEach(inject(function (_DialogLink_, _RetraiteDialog_, _$timeout_) {
        DialogLink = _DialogLink_;
        RetraiteDialog = _RetraiteDialog_;
        $timeout = _$timeout_;
    }));
    
    var chapitre = {};
    var selectedText = "cliquez ici";

    describe('check dialog calls ', function () {
        
        beforeEach(function() {
            spyOn(RetraiteDialog, 'display').and.returnValue({
                then: function() {}
            });
        });

        it('should display dialog', function () {

            DialogLink.display(selectedText);

            expect(RetraiteDialog.display).toHaveBeenCalled();
            var options = RetraiteDialog.display.calls.mostRecent().args[0];
            expect(options.title).toEqual("Insertion d'un lien");
            expect(options.templateUrl).toEqual('src/config/edit/dialogs/dialog-link/dialog-link.html');
            expect(options.value).toEqual({ text: selectedText });
        });

    });

    describe('check dialog canceled ', function () {
        
        beforeEach(function() {
            spyOn(RetraiteDialog, 'display').and.returnValue({
                then: function(onSuccess, onError) {
                    expect(onSuccess).toEqual(jasmine.any(Function));
                    expect(onError).toEqual(jasmine.any(Function));
                    onError();
                }
            });
        });

        it('should display dialog', function (done) {

            var promise = DialogLink.display(selectedText);

            promise.then(function onSucess() {
                jasmine.fail("Ne devrait pas venir ici !");
            }, function onError() {
                done();
            });
            $timeout.flush();
        });

    });

    describe('check dialog validated ', function () {
        
        beforeEach(function() {
            spyOn(RetraiteDialog, 'display').and.returnValue({
                then: function(onSuccess, onError) {
                    expect(onSuccess).toEqual(jasmine.any(Function));
                    expect(onError).toEqual(jasmine.any(Function));
                    var result = {
                        text: selectedText,
                        url: "http://monsite"
                    };
                    onSuccess(result);
                }
            });
        });

        it('should display dialog', function (done) {

            var promise = DialogLink.display(selectedText);

            promise.then(function onSucess(result) {
                expect(result.text).toEqual(selectedText);
                expect(result.url).toEqual("http://monsite");
                done();
            }, function onError() {
                jasmine.fail("Ne devrait pas venir ici !");
            });
            $timeout.flush();
        });


    });

});
