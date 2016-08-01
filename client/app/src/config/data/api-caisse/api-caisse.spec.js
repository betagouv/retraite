'use strict';

describe('ApiCaisse', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var ApiCaisse, $httpBackend;
    
    beforeEach(inject(function (_ApiCaisse_, _$httpBackend_) {

        ApiCaisse = _ApiCaisse_;
        $httpBackend = _$httpBackend_;
        
    }));
    
    beforeEach(function() {
    });

    describe('all', function () {

        it('should call REST server and return result', function (done) {

            var mockApiCaisses = [{
                nom: "CARSAT Aquitaine",
                adresse1: "80 avenue de la Jallère",
                adresse2: "Quartier du Lac",
                adresse3: "33053 BORDEAUX CEDEX",
                id: 29
            }, {
                nom: "CARSAT Auvergne",
                adresse1: "63036 CLERMONT FERRAND CEDEX 1",
                adresse2: "",
                adresse3: "",
                id: 30
            }];
            
            $httpBackend.expectGET('/api/caisse').respond(200, mockApiCaisses);
            
            var caisses = ApiCaisse.all();

            caisses.$promise.then(function onSuccess(data) {
                expect(data.length).toEqual(mockApiCaisses.length);
                expect(data[1].id).toEqual(mockApiCaisses[1].id);
                done();
            });
            $httpBackend.flush();
        });

    });

    describe('get', function () {

        it('should call REST server and return result', function (done) {

            var mockCaisse = {
                nom: "CARSAT Aquitaine",
                adresse1: "80 avenue de la Jallère",
                adresse2: "Quartier du Lac",
                adresse3: "33053 BORDEAUX CEDEX",
                id: 29
            };
            
            $httpBackend.expectGET('/api/caisse/29').respond(200, mockCaisse);
            
            var caisse = ApiCaisse.get(29);

            caisse.$promise.then(function onSuccess(data) {
                expect(data.id).toEqual(mockCaisse.id);
                done();
            });
            $httpBackend.flush(); 
        });

    });

    describe('save', function () {

        it('should call REST server and return result', function (done) {

            var mockCaisse = {
                nom: "CARSAT Aquitaine",
                adresse1: "80 avenue de la Jallère",
                adresse2: "Quartier du Lac",
                adresse3: "33053 BORDEAUX CEDEX",
                id: 29
            };
            
            $httpBackend.expectPUT('/api/caisse/29', mockCaisse).respond(200);
            
            var promise = ApiCaisse.save(mockCaisse);

            promise.then(function onSuccess(data) {
                done();
            });
            $httpBackend.flush();
        });

    });

});
