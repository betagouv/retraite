'use strict';

describe('WsCaisseDepartement', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var WsCaisseDepartement, $httpBackend;
    
    beforeEach(inject(function (_WsCaisseDepartement_, _$httpBackend_) {

        WsCaisseDepartement = _WsCaisseDepartement_;
        $httpBackend = _$httpBackend_;
        
    }));
    
    describe('deleteDepartement', function() {
        
        it('should call WS for deleteDepartement and resolve promise if success', function (done) {

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
    
    describe('addDepartement', function() {
        
        it('should call WS for addDepartement and resolve promise if success', function (done) {

            $httpBackend.expectGET('/ws/caissedepartement/adddepartement?checklistName=CNAV&caisseId=29&departement=14')
                .respond(200);

            WsCaisseDepartement.addDepartement('CNAV', 29, 14).then(function success() {
                done();
            }, function error() {
                // Ne devrait pas venir ici
            });

            $httpBackend.flush();
        });

        it('should call WS for addDepartement and reject promise if error', function (done) {

            $httpBackend.expectGET('/ws/caissedepartement/adddepartement?checklistName=CNAV&caisseId=29&departement=14')
                .respond(500);

            WsCaisseDepartement.addDepartement('CNAV', 29, 14).then(function success() {
                // Ne devrait pas venir ici
            }, function error() {
                done();
            });

            $httpBackend.flush();
        });
        
    });
    
    describe('addCaisse', function() {
        
        it('should call WS for addCaisse and resolve promise if success', function (done) {

            $httpBackend.expectGET('/ws/caissedepartement/addcaisse?checklistName=CNAV&departement=14')
                .respond({caisseId:92});

            WsCaisseDepartement.addCaisse('CNAV', 14).then(function success(data) {
                expect(data.caisseId).toEqual(92);
                done();
            }, function error() {
                // Ne devrait pas venir ici
            });

            $httpBackend.flush();
        });

        it('should call WS for addCaisse and reject promise if error', function (done) {

            $httpBackend.expectGET('/ws/caissedepartement/addcaisse?checklistName=CNAV&departement=14')
                .respond(500);

            WsCaisseDepartement.addCaisse('CNAV', 14).then(function success() {
                // Ne devrait pas venir ici
            }, function error() {
                done();
            });

            $httpBackend.flush();
        });
        
    });
    
});


