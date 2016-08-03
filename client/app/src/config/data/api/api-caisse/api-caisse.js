'use strict';

angular.module('SgmapRetraiteConfig').factory('ApiCaisse', function ($resource, $q) {
    var resource = $resource('/api/caisse/:id', {
        id: '@id'
    }, {
        save: {
            method: 'PUT'
        }
    });
    
    return {
        all: function() {
            return resource.query();
        },
        get: function(id) {
            return resource.get({id:id});
        },
        save: function(caisse) {
            var deferred = $q.defer();
            resource.save(caisse, function onSuccess(caisseSaved) {
                deferred.resolve(caisseSaved);
            });
            return deferred.promise;
        }
    };
});
