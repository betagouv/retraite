'use strict';

angular.module('SgmapRetraiteConfig').service('UserService', function ($http, httpBuffer, RetraiteToaster, $state, $rootScope) {

    var isLogging = false;
    
    this.login = function (user) {
        isLogging = true;
        return $http.post('/login?username='+user.name+'&password='+user.pass)
            .success(function (response) {
                isLogging = false;
                $rootScope.$broadcast('userLogged');
                $state.go('configlist');
                httpBuffer.retryLastRequest();
            })
            .error(function(data, status, headers, config ) {
                isLogging = false;
                RetraiteToaster.popErrorWithTimeout("Erreur d'identification !");
            });
    };

    this.logout = function () {
        $http.get('/logout').success(function() {
            $state.reload();
        });
    };

    this.isLogging = function() {
        return isLogging;
    };
});
