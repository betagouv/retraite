'use strict';

describe('CheckList', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var CheckList, $httpBackend, HttpContextProvider;
    
    beforeEach(inject(function (_CheckList_, _$httpBackend_, _HttpContextProvider_) {

        CheckList = _CheckList_;
        $httpBackend = _$httpBackend_;
        HttpContextProvider = _HttpContextProvider_;
        
    }));
    
    beforeEach(function() {
        spyOn(HttpContextProvider, 'getHttpContext').and.returnValue('/retraite');
    });
    
    describe('all', function () {

        it('should call REST server and return result', function () {

            var mockCheckLists = [{id:1},{id:2}];
            $httpBackend.expectGET('/retraite/api/checklist').respond(200, mockCheckLists);
            
            var checklists = CheckList.all();

            checklists.$promise.then(function onSuccess(checklistsLoaded) {
                expect(checklistsLoaded.length).toEqual(mockCheckLists.length); 
                expect(checklistsLoaded[1].id).toEqual(mockCheckLists[1].id);
            });
            $httpBackend.flush();
        });

    });

    describe('get', function () {

        it('should call REST server and return result', function () {

            var mockCheckList = {id:2};
            
            $httpBackend.expectGET('/retraite/api/checklist/2').respond(200, mockCheckList);
            
            var checklist = CheckList.get(2);

            checklist.$promise.then(function onSuccess(data) {
                expect(data.id).toEqual(mockCheckList.id);
            });
            $httpBackend.flush();
        });

    });

    describe('save', function () {

        it('should call REST server and return result', function () {

            var checkList = {id:2, nom: 'toto'};    
            $httpBackend.expectPUT('/retraite/api/checklist/2', {id:2, nom:'toto'}).respond(200);
            
            CheckList.save(checkList);

            $httpBackend.flush();
        });

    });

});
