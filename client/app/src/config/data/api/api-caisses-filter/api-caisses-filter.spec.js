'use strict';

describe('ApiCaisseFilter', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var ApiCaisseFilter, $httpBackend;
    
    beforeEach(inject(function (_ApiCaisseFilter_, _$httpBackend_) {

        ApiCaisseFilter = _ApiCaisseFilter_;
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
            
            $httpBackend.expectGET('/api/caissefilter/CNAV').respond(200, mockApiCaisses);
            
            var caisses = ApiCaisseFilter.allForChecklistName("CNAV");

            caisses.$promise.then(function onSuccess(data) {
                expect(data.length).toEqual(mockApiCaisses.length);
                expect(data[1].id).toEqual(mockApiCaisses[1].id);
                done();
            });
            $httpBackend.flush();
        });

    });

});
