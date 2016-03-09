'use strict';

angular.module('SgmapRetraiteConfig').factory('HttpInterceptor', function ($q, RetraiteToaster, $injector, httpBuffer) {

    return {

        responseError: function (rejection) {

            var UserService = $injector.get('UserService');
            
            if (!UserService.isLogging()) {
                
                // Gestion erreur 401 (authentification)
                if (rejection.status === 401) {
                    console.log("Erreur 401, on redirige vers 'login'...");
                    var $state = $injector.get('$state');
                    $state.go('login');
                    var deferred = $q.defer();
                    httpBuffer.storeRequest({
                        config: rejection.config,
                        deferred: deferred
                    });
                    return deferred.promise;
                }

                // Popup pour notifier l'erreur
                var body = "<pre>Requête : " + rejection.config.method + " " + rejection.config.url + "\nErreur : " + rejection.data+"</pre>";
                RetraiteToaster.popError("Erreur lors d'un accès au serveur !", body);
            
            }

            return $q.reject(rejection);
        }
    };
 });