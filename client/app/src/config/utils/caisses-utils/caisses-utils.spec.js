'use strict';

describe('CaissesUtils', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var CaissesUtils;

    beforeEach(inject(function (_CaissesUtils_) {
        CaissesUtils = _CaissesUtils_;
    }));

    describe('searchAvailableDepartements', function () {
        
        it('should return availables departements', function () {

            var caissesDepartementales = [{
                departements: ["06", "07", "14", "15", "16", "17", "38", "49", "50",
                               "51", "52", "53", "54", "55", "56", "57", "58", "59", 
                               "60", "61", "62", "63", "64", "65", "66", "74"],
                nom: "MSA Alpes du Nord",
            }, {
                departements: ["01", "02", "04", "05", "84", "85", "86", "87", "88"],
                nom: "MSA Alpes-Vaucluse",
            }, {
                departements: ["18", "19", "2A", "21", "22", "23", "24", "25", "26", 
                               "27", "28", "29", "30", "31", "32", "33", "34", "35", 
                               "36", "37"],
                nom: "MSA Alpes-Vaucluse",
            }, {
                departements: ["67", "68", "69", "70", "71", "72", "73", "74", "75",
                              "76", "77", "78", "79", "80", "81", "82", "83", "89",
                              "90", "91", "92", "93", "94", "95"],
                nom: "MSA Alsace",
            }, {
                departements: ["971", "72", "974"],
                nom: "MSA Alsace",
            }];
            
            var departements = CaissesUtils.searchAvailableDepartements(caissesDepartementales);

            expect(departements).toEqual(["03", "08", "09", "10", "11", "12", "13", "2B",
                                         "39", "40", "41", "42", "43", "44", "45", 
                                         "46", "47", "48", "972", "973", "976"]);
        });
        
    });

});
