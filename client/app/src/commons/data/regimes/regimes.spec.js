'use strict';

describe('ApiRegimes', function () {
    
    beforeEach(module('SgmapRetraiteCommons'));
    
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


