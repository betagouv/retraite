'use strict';

describe('EditCaisseCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var $rootScope, $scope, controller, ApiCaisse;
    
    beforeEach(inject(function (_ApiCaisse_) {
        ApiCaisse = _ApiCaisse_;
    }));
    
    var mockCaisse = {
        "nom": "CARSAT Aquitaine",
        "adresse1": "80 avenue de la Jallère",
        "adresse2": "Quartier du Lac",
        "adresse3": "33053 BORDEAUX CEDEX",
        "id": 29
    };

    beforeEach(function() {
        spyOn(ApiCaisse, 'get').and.callFake(function(id) {
            if (id == 29) {
                return {
                    $promise: {
                        then: function(onSuccess, onError) {
                            onSuccess(mockCaisse);
                        }
                    }
                };
            }
            throw new Error("Situation innatendue !");
        });
    });

    beforeEach(inject(function (_$rootScope_, $controller) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        var $stateParams = {
            name: "CNAV",
            id: 29
        };
        
        controller = $controller('EditCaisseCtrl', {
            $scope: $scope,
            $stateParams: $stateParams
        });

    }));
    
    it('should have init data in scope', function () {
        expect($scope.caisse).toEqual(mockCaisse);
    });

    describe('save', function() {
        
        var caisseSaved = false;
        
        it('should have init data in scope', function () {
            
            spyOn(ApiCaisse, 'save');
            spyOn($rootScope, '$broadcast');

            $scope.save(mockCaisse);
            
            expect(ApiCaisse.save).toHaveBeenCalledWith(mockCaisse);
            expect($rootScope.$broadcast).toHaveBeenCalledWith('caisseSaved');
        });

    });
    
});

