'use strict';

describe('CaissesCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var $rootScope, $scope, controller, ApiCaisseFilter;
    
    beforeEach(inject(function (_ApiCaisseFilter_) {
        ApiCaisseFilter = _ApiCaisseFilter_;
    }));
    
    var mockCaisses = [{
        "nom": "CARSAT Aquitaine",
        "adresse1": "80 avenue de la Jallère",
        "adresse2": "Quartier du Lac",
        "adresse3": "33053 BORDEAUX CEDEX",
        "id": 29
    }, {
        "nom": "CARSAT Auvergne",
        "adresse1": "63036 CLERMONT FERRAND CEDEX 1",
        "adresse2": "",
        "adresse3": "",
        "id": 30
    }];

    beforeEach(function() {
        spyOn(ApiCaisseFilter, 'allForChecklistName').and.callFake(function(name) {
            
            if (name == "CNAV") {
                return {
                    $promise: {
                        then: function(onSuccess, onError) {
                            onSuccess(mockCaisses);
                        }
                    }
                };
            }
            
        });
    });

    beforeEach(inject(function (_$rootScope_, $controller) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        var $stateParams = {
            name: "CNAV"
        };
        
        controller = $controller('CaissesCtrl', {
            $scope: $scope,
            $stateParams: $stateParams
        });

    }));
    
    it('should have init data in scope', function () {
        expect($scope.caisses).toEqual(mockCaisses);
    });

    it('should refresh if event caisseSaved', function () {
        
        $rootScope.$broadcast('caisseSaved');
        
        expect(ApiCaisseFilter.allForChecklistName.calls.count()).toEqual(2);
    });

});

