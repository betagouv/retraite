'use strict';

describe('SyntaxAnalyzer', function () {

    beforeEach(module('SgmapRetraiteCommons'));

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
