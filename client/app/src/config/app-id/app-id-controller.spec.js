'use strict';

describe('AppIdCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
        
    it('should set appId=recette', inject(function ($controller, Wrapper) {
        
        spyOn(Wrapper, 'getLocation').and.returnValue({
            host: 'recette-retraite.sgmap.fr'
        });
        
        var controller = $controller('AppIdCtrl', {$scope: {}});

        expect(controller.appId).toEqual('recette');
    }));

    it('should set appId=localhost', inject(function ($controller, Wrapper) {
        
        spyOn(Wrapper, 'getLocation').and.returnValue({
            host: 'localhost:9000'
        });
        
        var controller = $controller('AppIdCtrl', {$scope: {}});

        expect(controller.appId).toEqual('localhost');
    }));

    it('should set appId=prod', inject(function ($controller, Wrapper) {
        
        spyOn(Wrapper, 'getLocation').and.returnValue({
            host: 'retraite.sgmap.fr'
        });
        
        var controller = $controller('AppIdCtrl', {$scope: {}});

        expect(controller.appId).toEqual('prod');
    }));

});

