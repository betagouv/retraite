'use strict';

describe('LogoutCtrl', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
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
