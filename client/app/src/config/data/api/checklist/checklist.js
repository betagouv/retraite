'use strict';

angular.module('SgmapRetraiteConfig').factory('CheckList', function ($resource, $q) {
    var resourceForCheckList = $resource('/api/checklist/:id', {
        id: '@id'
    }, {
        save: {
            method: 'PUT'
        }
    });
    
    return {
        all: function() {
            return resourceForCheckList.query();
        },
        get: function(id) {
            return resourceForCheckList.get({id:id});
        },
        save: function(checkList) {
            var deferred = $q.defer();
            resourceForCheckList.save(checkList, function onSuccess(checkListSaved) {
                deferred.resolve(checkListSaved);
            });
            return deferred.promise;
        }
    };
});
