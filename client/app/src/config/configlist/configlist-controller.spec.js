'use strict';

describe('ConfigListCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var $scope, CheckList;
    
    beforeEach(inject(function ($rootScope, $controller, _CheckList_) {

        CheckList = _CheckList_;
        
    }));
    
    var mockCheckLists = [{id:1},{id:2}];

    beforeEach(function() {
        spyOn(CheckList, 'all').and.returnValue(mockCheckLists);
    });

    // Création du contrôleur
    
    beforeEach(inject(function ($rootScope, $controller, _CheckList_) {

        $scope = $rootScope.$new();
        $controller('ConfigListCtrl', {
            $scope: $scope            
        });

    }));
    
    // Test(s)
    
    it('should have init data in scope', function () {
        expect($scope.checklists).toEqual(mockCheckLists);
    });

});
