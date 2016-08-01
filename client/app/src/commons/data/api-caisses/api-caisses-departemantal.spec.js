'use strict';

describe('ApiCaisseDepartementale', function () {
    
    beforeEach(module('SgmapRetraiteCommons'));
    
    var ApiCaisseDepartementale, $httpBackend;
    
    beforeEach(inject(function (_ApiCaisseDepartementale_, _$httpBackend_) {

        ApiCaisseDepartementale = _ApiCaisseDepartementale_;
        $httpBackend = _$httpBackend_;
        
    }));
    
    beforeEach(function() {
    });

    describe('all', function () {

        it('should call REST server and return result', function (done) {

            var mockApiCaisseDepartementales = [{
                id:1,
                checklistName: "CNAV",
                departement: "01",
                caisse: {
                    id:1,
                    nom: "Caisse 1",
                    adresse1:"adresse, ville"
                }
            },{
                id:2,
                checklistName: "CNAV",
                departement: "02",
                caisse: {
                    id:1,
                    nom: "Caisse 1",
                    adresse1:"adresse, ville"
                }
            }];
            
            $httpBackend.expectGET('/api/caissedepartementale/CNAV').respond(200, mockApiCaisseDepartementales);
            
            var ApiCaisseDepartementales = ApiCaisseDepartementale.all("CNAV");

            ApiCaisseDepartementales.$promise.then(function onSuccess(data) {
                expect(data.length).toEqual(mockApiCaisseDepartementales.length);
                expect(data[1].id).toEqual(mockApiCaisseDepartementales[1].id);
                done();
            });
            $httpBackend.flush();
        });

    });

    /*describe('get', function () {

        it('should call REST server and return result', function (done) {

            var mockApiCaisseDepartementale = {id:2};
            
            $httpBackend.expectGET('/api/ApiCaisseDepartementale/2').respond(200, mockApiCaisseDepartementale);
            
            var ApiCaisseDepartementale = ApiCaisseDepartementale.get(2);

            ApiCaisseDepartementale.$promise.then(function onSuccess(data) {
                expect(data.id).toEqual(mockApiCaisseDepartementale.id);
                done();
            });
            $httpBackend.flush();
        });

    });

    describe('save', function () {

        it('should call REST server and return result', function (done) {

            var ApiCaisseDepartementale = {id:2, nom: 'toto'};
            
            $httpBackend.expectPUT('/api/ApiCaisseDepartementale/2', {id:2, nom:'toto'}).respond(200);
            
            var promise = ApiCaisseDepartementale.save(ApiCaisseDepartementale);

            promise.then(function onSuccess(data) {
                done();
            });
            $httpBackend.flush();
        });

    });*/

});
