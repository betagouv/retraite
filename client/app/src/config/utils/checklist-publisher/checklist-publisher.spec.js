'use strict';

describe('ChecklistPublisher', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var ChecklistPublisher, $httpBackend;
    
    beforeEach(inject(function (_ChecklistPublisher_, _$httpBackend_) {

        ChecklistPublisher = _ChecklistPublisher_;
        $httpBackend = _$httpBackend_;
        
    }));
    
    describe('publish', function () {

        it('should call REST server and return result', function () {
            
            $httpBackend.expectGET('/api/checklistpublish?id=8').respond(200);
            var success=false;
            
            ChecklistPublisher.publish(8).then(function() {
                success = true;
            });

            $httpBackend.flush();
            expect(success).toBeTruthy();
        });

    });

    afterEach(function() {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

});
