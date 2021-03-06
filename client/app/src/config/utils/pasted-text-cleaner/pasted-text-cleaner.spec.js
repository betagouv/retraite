'use strict';

describe('PastedTextCleaner', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var PastedTextCleaner;

    beforeEach(inject(function (_PastedTextCleaner_) {
        PastedTextCleaner = _PastedTextCleaner_;
    }));

    describe('replace', function () {
        
        it('should clean the diff text', function () {

            var oldText = "<p>qd</p><p>qsd&nbsp;</p><p>azeaze</p><p><br></p>";
            var newText = "<p>qd</p><p>qsd&nbsp;</p><div class=\"row\"><br>&nbsp;lol&nbsp;&nbsp;&nbsp;aze&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class=\"col-xs-3\"></div></div><p>azeaze</p><p><br></p>";
            
            var result = PastedTextCleaner.clean(oldText, newText);

            var expected = "<p>qd</p><p>qsd&nbsp;</p> lol   aze        <p>azeaze</p><p><br></p>";
            expect(result).toEqual(expected);
        });
        
    });

});
