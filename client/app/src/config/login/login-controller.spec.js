'use strict';

describe('LoginCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var $scope, controller, UserService, $state;
    
    beforeEach(inject(function ($rootScope, $controller, _UserService_, _$state_) {

        UserService = _UserService_;
        $state = _$state_;
        
        $scope = $rootScope.$new();
        controller = $controller('LoginCtrl', {
            $scope: $scope
        });
        
    }));

    it('should init data', function () {

        expect($scope.user).toEqual({});

    });

    describe('login', function () {

        it('should call UserService.login', function () {

            var user = {
                login: 'user',
                password: 'pwd'
            };
            $scope.user = user;
            spyOn(UserService, 'login');
            
            $scope.login();

            expect(UserService.login).toHaveBeenCalledWith(user);
        });
        
    }); 
    
    describe('test', function () {

        it('should go to state *alvar-test*', function () {

            spyOn($state, 'go');
            
            $scope.test();

            expect($state.go).toHaveBeenCalledWith('alvar-test');
        });
        
    });
    
});
//            var UserService = $injector.get('UserService');
            //UserService.tryLoginWithStoredUser();
