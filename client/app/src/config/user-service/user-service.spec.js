'use strict';

describe('UserService', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var UserService, $httpBackend, RetraiteToaster, httpBuffer, $state, $timeout;
    
    beforeEach(inject(function (_UserService_, _$httpBackend_, _RetraiteToaster_, _httpBuffer_, _$state_, _$timeout_) {
        UserService = _UserService_;
        $httpBackend = _$httpBackend_;
        RetraiteToaster = _RetraiteToaster_;
        httpBuffer = _httpBuffer_;
        $state = _$state_;
        $timeout = _$timeout_;
    }));
    
    beforeEach(function () {
        spyOn(RetraiteToaster, 'popError');
        spyOn(RetraiteToaster, 'popErrorWithTimeout');
        spyOn(httpBuffer, 'retryLastRequest');
        spyOn($state, 'go');
        spyOn($state, 'reload');
    });

    var user = {
        name: 'xnopre',
        pass: 'passxnopre'
    };

    describe('login', function () {
        
        it('should process login success', function (done) {

            $httpBackend.expectPOST('/login?username=xnopre&password=passxnopre').respond(200);

            UserService.login(user).then(function() {
                expect($state.go).toHaveBeenCalledWith('configlist');
                expect(httpBuffer.retryLastRequest).toHaveBeenCalled();
                expect(UserService.isLogging()).toBeFalsy();
                done();
            });

            expect(UserService.isLogging()).toBeTruthy();
            $httpBackend.flush();
        });

        it('should process login error', function () {

            $httpBackend.expectPOST('/login?username=xnopre&password=passxnopre').respond(401);

            UserService.login(user);

            $httpBackend.flush();
            expect($state.go).not.toHaveBeenCalled();
            expect(httpBuffer.retryLastRequest).not.toHaveBeenCalled();
            expect(RetraiteToaster.popErrorWithTimeout).toHaveBeenCalledWith("Erreur d'identification !");
        });
        
    });

    describe('logout', function () {
        
        it('should process logout', function () {

            $httpBackend.expectGET('/logout').respond(200);

            UserService.logout();

            $httpBackend.flush();
            expect($state.reload).toHaveBeenCalled();
        });
        
    });

});
