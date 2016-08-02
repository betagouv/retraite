'use strict';

describe('WsCaisseDepartement', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var WsCaisseDepartement, $httpBackend;
    
    beforeEach(inject(function (_WsCaisseDepartement_, _$httpBackend_) {

        WsCaisseDepartement = _WsCaisseDepartement_;
        $httpBackend = _$httpBackend_;
        
    }));
    

    it('should call WS for deleteDepartement and resolve promise if success', function (done) {
        
        var mockRegimesBaseAlignes = ["CAVIMAC","CNAV","MSA","RSI"];
        
        $httpBackend.expectGET('/ws/caissedepartement/deletedepartement?caisseId=29&departement=14')
            .respond(200);
        
        WsCaisseDepartement.deleteDepartement(29, 14).then(function success() {
            done();
        }, function error() {
            // Ne devrait pas venir ici
        });
        
        $httpBackend.flush();
    });

    it('should call WS for deleteDepartement and reject promise if error', function (done) {
        
        var mockRegimesBaseAlignes = ["CAVIMAC","CNAV","MSA","RSI"];
        
        $httpBackend.expectGET('/ws/caissedepartement/deletedepartement?caisseId=29&departement=14')
            .respond(500);
        
        WsCaisseDepartement.deleteDepartement(29, 14).then(function success() {
            // Ne devrait pas venir ici
        }, function error() {
            done();
        });
        
        $httpBackend.flush();
    });

});


