'use strict';

describe('ApiUserChecklist', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var ApiUserChecklist, $httpBackend;
    
    beforeEach(inject(function (_ApiUserChecklist_, _$httpBackend_) {

        ApiUserChecklist = _ApiUserChecklist_;
        $httpBackend = _$httpBackend_;
        
    }));
    

    it('should getChecklist', function (done) {
        
        var data = {
            nom: "DUPONT",
            dateNaissance: "07/10/1954",
            nir: "1541014123456",
            regimes: [{ name:"CAVIMAC"},{ name:"CNAV" }]
        };
        
        $httpBackend.whenGET('/apiuserchecklist/getuserchecklist?nom=DUPONT&dateNaissance=07/10/1954&nir=1541014123456&regimes=CAVIMAC,CNAV&published=false')
            .respond('bla bla bla');
        
        ApiUserChecklist.getChecklist(data).then(function onSuccess(result) {
            expect(result).toEqual('bla bla bla');
            done();
        });
        
        $httpBackend.flush();
    });

    it('should getChecklistUrl not published not full', function () {
        
        var data = {
            nom: "DUPONT",
            dateNaissance: "07/10/1954",
            nir: "1541014123456",
            regimes: [{ name:"CAVIMAC"},{ name:"CNAV" }]
        };
        
        var url = ApiUserChecklist.getChecklistUrl(data);
        
        expect(url).toEqual('/apiuserchecklist/getuserchecklist?nom=DUPONT&dateNaissance=07/10/1954&nir=1541014123456&regimes=CAVIMAC,CNAV&published=false');
    });

    it('should getChecklistUrl published and full html page', function () {
        
        var data = {
            nom: "DUPONT",
            dateNaissance: "07/10/1954",
            nir: "1541014123456",
            regimes: [{ name:"CAVIMAC"},{ name:"CNAV" }]
        };
        
        var url = ApiUserChecklist.getChecklistUrl(data, true, true);
        
        expect(url).toEqual('/apiuserchecklist/getuserchecklist?nom=DUPONT&dateNaissance=07/10/1954&nir=1541014123456&regimes=CAVIMAC,CNAV&published=true&full=true');
    });

});


