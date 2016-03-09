'use strict';

angular.module('SgmapRetraiteCommons').service('ApiRegimes', function ($q, $http) {
 
    this.getRegimes = function() {
        var deferred = $q.defer();
        $http.get('/apirestregimes/getregimes')
            .then(function(data) {
                deferred.resolve(data.data);
            }, function onError(error) {
                deferred.reject(error);
            });
        return deferred.promise;        
    };
    
});
