'use strict';

describe('HttpInterceptor', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var HttpInterceptor, RetraiteToaster, $q, $state, httpBuffer, UserService;
    
    beforeEach(inject(function (_HttpInterceptor_, _RetraiteToaster_, _$q_, _$state_, _httpBuffer_, _UserService_) {
        HttpInterceptor = _HttpInterceptor_;
        RetraiteToaster = _RetraiteToaster_;
        $q = _$q_;
        $state = _$state_;
        httpBuffer = _httpBuffer_;
        UserService = _UserService_;

        spyOn($q, 'reject').and.returnValue('rejectResult');
        spyOn(RetraiteToaster, "popError");
        spyOn(httpBuffer, 'storeRequest');
        spyOn($state, 'go');
    }));

    it('should popup toaster for error', function() {
        
        var rejection = { 
            status: 500, 
            config: {
                method: 'get',
                url: 'monurl'
            }, 
            data: 'erreur bidon' 
        };
        
        var result = HttpInterceptor.responseError(rejection);
        
        expect(result).toEqual('rejectResult');
        expect($q.reject).toHaveBeenCalledWith(rejection);
        expect(RetraiteToaster.popError).toHaveBeenCalledWith("Erreur lors d'un accès au serveur !", 
                "<pre>Requête : get monurl\nErreur : erreur bidon</pre>");
    });
    
    it('should intercept 401', function() {

        spyOn(UserService, 'isLogging').and.returnValue(false);
        
        var rejection = { status: 401, config: {} };
        var result = HttpInterceptor.responseError(rejection);
        
        expect(isPromise(result)).toBeTruthy();
        expect($state.go).toHaveBeenCalledWith('login');
        var tester = {
            asymmetricMatch: function(actual) {
                return actual.config == rejection.config && isDefer(actual.deferred);
            }
        };
        expect(httpBuffer.storeRequest).toHaveBeenCalledWith(tester);
        expect(RetraiteToaster.popError).not.toHaveBeenCalled(); 
    });

    it('should do nothing if 401 while logging', function() {
        
        spyOn(UserService, 'isLogging').and.returnValue(true);

        var rejection = { status: 401, config: {} };
        var result = HttpInterceptor.responseError(rejection);
        
        expect(result).toEqual('rejectResult');
        expect($q.reject).toHaveBeenCalledWith(rejection);
        expect($state.go).not.toHaveBeenCalled();
        expect(httpBuffer.storeRequest).not.toHaveBeenCalled();
        expect(RetraiteToaster.popError).not.toHaveBeenCalled();
    });
    
    var isPromise = function(value) {
        return value.$$state !== undefined;
    };
    
    var isDefer = function(value) {
        return value.promise !== undefined;
    };
});