'use strict';

describe('ChecklistPublisher', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

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
