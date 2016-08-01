'use strict';

angular.module('SgmapRetraiteCommons').service('ApiCaisseDepartementale', function ($q, $http) {
 
    this.getCaisses = function() {
        var deferred = $q.defer();
        $http.get('/apirestcaisses/getcaisses')
            .then(function(data) {
                deferred.resolve(data.data);
            }, function onError(error) {
                deferred.reject(error);
            });
        return deferred.promise;
    };
    
});


angular.module('SgmapRetraiteCommons').factory('ApiCaisseDepartementale', function ($resource, $q) {
    var resource = $resource('/api/caissedepartementale/:name/:id', {
        id: '@id'
    }, {
        save: {
            method: 'PUT'
        }
    });
    
    return {
        all: function(name) {
            return resource.query({name:name});
        }/*,
        get: function(id) {
            return resourceForCheckList.get({id:id});
        },
        save: function(checkList) {
            var deferred = $q.defer();
            resourceForCheckList.save(checkList, function onSuccess(checkListSaved) {
                deferred.resolve(checkListSaved);
            });
            return deferred.promise;
        }*/
    };
});
