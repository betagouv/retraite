'use strict';

describe('CheckList', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var CheckList, $httpBackend;
    
    beforeEach(inject(function (_CheckList_, _$httpBackend_) {

        CheckList = _CheckList_;
        $httpBackend = _$httpBackend_;
        
    }));
    
    beforeEach(function() {
    });

    describe('all', function () {

        it('should call REST server and return result', function (done) {

            var mockCheckLists = [{id:1},{id:2}];
            
            $httpBackend.expectGET('/api/checklist').respond(200, mockCheckLists);
            
            var checklists = CheckList.all();

            checklists.$promise.then(function onSuccess(data) {
                expect(data.length).toEqual(mockCheckLists.length);
                expect(data[1].id).toEqual(mockCheckLists[1].id);
                done();
            });
            $httpBackend.flush();
        });

    });

    describe('get', function () {

        it('should call REST server and return result', function (done) {

            var mockCheckList = {id:2};
            
            $httpBackend.expectGET('/api/checklist/2').respond(200, mockCheckList);
            
            var checklist = CheckList.get(2);

            checklist.$promise.then(function onSuccess(data) {
                expect(data.id).toEqual(mockCheckList.id);
                done();
            });
            $httpBackend.flush();
        });

    });

    describe('save', function () {

        it('should call REST server and return result', function (done) {

            var checkList = {id:2, nom: 'toto'};
            
            $httpBackend.expectPUT('/api/checklist/2', {id:2, nom:'toto'}).respond(200);
            
            var promise = CheckList.save(checkList);

            promise.then(function onSuccess(data) {
                done();
            });
            $httpBackend.flush();
        });

    });

});
