'use strict';

angular.module('SgmapRetraiteConfig').service('WsCaisseDepartement', function ($q, $http) {
 
    this.deleteDepartement = function(caisseId, departement) {
        var deferred = $q.defer();
        $http.get('/ws/caissedepartement/deletedepartement?caisseId='+caisseId+"&departement="+departement)
            .then(function() {
                deferred.resolve();
            }, function onError(error) {
                deferred.reject(error);
            });
        return deferred.promise;
    };
    
});
