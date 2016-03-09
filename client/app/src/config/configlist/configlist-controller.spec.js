'use strict';

describe('ConfigListCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
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
