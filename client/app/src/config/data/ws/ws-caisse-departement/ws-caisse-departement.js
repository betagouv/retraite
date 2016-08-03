'use strict';

angular.module('SgmapRetraiteConfig').service('WsCaisseDepartement', function ($q, $http) {
 
    this.deleteDepartement = function(caisseId, departement) {
        return callWs('deletedepartement', caisseId, departement);
    };
    
    this.addDepartement = function(checklistName, caisseId, departement) {
        return callWs('adddepartement', caisseId, departement, checklistName);
    };
    
    this.addCaisse = function(checklistName, departement) {
        return callWs('addcaisse', null, departement, checklistName);
    };
    
    // Privé
    
    function callWs(operation, caisseId, departement, checklistName) {
        var url = '/ws/caissedepartement/'+operation+'?';
        if (checklistName) {
            url += 'checklistName='+checklistName+'&';
        }
        if (caisseId) {
            url += 'caisseId='+caisseId+'&';
        }
        url += 'departement='+departement;
        
        var deferred = $q.defer();
        $http.get(url)
            .then(function(respond) {
                deferred.resolve(respond.data);
            }, function onError(error) {
                deferred.reject(error);
            });
        return deferred.promise;
    }
});
