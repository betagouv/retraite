'use strict';

describe('DialogVariables', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    var DialogVariables, RetraiteDialog, $timeout;

    beforeEach(inject(function (_DialogVariables_, _RetraiteDialog_, _$timeout_) {
        DialogVariables = _DialogVariables_;
        RetraiteDialog = _RetraiteDialog_;
        $timeout = _$timeout_;
    }));
    
    var chapitre = {};

    describe('check dialog calls ', function () {
        
        beforeEach(function() {
            spyOn(RetraiteDialog, 'display').and.returnValue({
                then: function() {}
            });
        });

        it('should display dialog', function () {

            DialogVariables.display();

            expect(RetraiteDialog.display).toHaveBeenCalled();
            var options = RetraiteDialog.display.calls.mostRecent().args[0];
            expect(options.title).toEqual("Insertion d'une variable");
            expect(options.templateUrl).toEqual('src/config/edit/dialogs/dialog-variables/dialog-variables.html');
            expect(options.value).toEqual({ variable: 'regimes_base_hors_alignes' });
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

            var promise = DialogVariables.display();

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
                        variable: "var-selected"
                    };
                    onSuccess(result);
                }
            });
        });

        it('should display dialog', function (done) {

            var promise = DialogVariables.display();

            promise.then(function onSucess(variable) {
                expect(variable).toEqual("var-selected");
                done();
            }, function onError() {
                jasmine.fail("Ne devrait pas venir ici !");
            });
            $timeout.flush();
        });


    });

});
