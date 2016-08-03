'use strict';

describe('ApiRegimes', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var ApiRegimes, $httpBackend;
    
    beforeEach(inject(function (_ApiRegimes_, _$httpBackend_) {

        ApiRegimes = _ApiRegimes_;
        $httpBackend = _$httpBackend_;
        
    }));
    

    it('should getRegimes', function (done) {
        
        var mockRegimesBaseAlignes = ["CAVIMAC","CNAV","MSA","RSI"];
        
        $httpBackend.whenGET('/apirestregimes/getregimes')
            .respond(mockRegimesBaseAlignes);
        
        ApiRegimes.getRegimes().then(function onSuccess(data) {
            expect(data).toEqual(mockRegimesBaseAlignes);
            done();
        });
        
        $httpBackend.flush();
    });

});


