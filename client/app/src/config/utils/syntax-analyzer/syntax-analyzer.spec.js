'use strict';

describe('SyntaxAnalyzer', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var SyntaxAnalyzer;

    beforeEach(inject(function (_SyntaxAnalyzer_) {
        SyntaxAnalyzer = _SyntaxAnalyzer_;
    }));

    describe('replace', function () {
        
        it('should return true/false depending on text error', function () {

            // False
            expect(SyntaxAnalyzer.isSyntaxError()).toBeFalsy();
            expect(SyntaxAnalyzer.isSyntaxError("debut fin")).toBeFalsy();
            expect(SyntaxAnalyzer.isSyntaxError("debut {{var1}} fin")).toBeFalsy();
            expect(SyntaxAnalyzer.isSyntaxError("debut [[var1]] fin")).toBeFalsy();
            
            // True
            // Manque }}
            expect(SyntaxAnalyzer.isSyntaxError("debut {{var1 fin")).toBeTruthy();
            expect(SyntaxAnalyzer.isSyntaxError("debut {{var1}}{{var2 fin")).toBeTruthy();
            expect(SyntaxAnalyzer.isSyntaxError("debut {{var1{{var2}} fin")).toBeTruthy();
            expect(SyntaxAnalyzer.isSyntaxError("debut [[var1 fin")).toBeTruthy();
            
        });

    });
});
