'use strict';

describe('ConfigListCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var $scope, controller, CheckList;
    
    beforeEach(inject(function ($rootScope, $controller, _CheckList_) {

        CheckList = _CheckList_;
        
    }));
    
    var mockCheckLists = [{id:1},{id:2}];

    beforeEach(function() {
        spyOn(CheckList, 'all').and.returnValue(mockCheckLists);
    });

    beforeEach(inject(function ($rootScope, $controller, _CheckList_) {

        $scope = $rootScope.$new();
        controller = $controller('ConfigListCtrl', {
            $scope: $scope            
        });

    }));
    
    it('should have init data in scope', function () {
        expect($scope.checklists).toEqual(mockCheckLists);
    });

});
