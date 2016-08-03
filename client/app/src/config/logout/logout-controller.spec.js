'use strict';

describe('LogoutCtrl', function () {
    
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
        controller = $controller('LogoutCtrl', {
            $scope: $scope
        });
        
    }));

    describe('logout', function () {

        it('should call UserService.login', function () {

            spyOn(UserService, 'logout');
            
            $scope.logout();

            expect(UserService.logout).toHaveBeenCalled();
        });
        
    }); 
    
    
});
//            var UserService = $injector.get('UserService');
            //UserService.tryLoginWithStoredUser();
