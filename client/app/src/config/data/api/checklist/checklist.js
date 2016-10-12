'use strict';

angular.module('SgmapRetraiteConfig').service('CheckList', function ($resource, $q, HttpContextProvider) {
    
    var resourceForCheckList;
    
    this.all = function() {
        return getResourceForCheckList().query();
    };
    
    this.get = function(id) {
        return getResourceForCheckList().get({id:id});
    };
        
    this.save = function(checkList) {
        var deferred = $q.defer();
        getResourceForCheckList().save(checkList, function onSuccess(checkListSaved) {
            deferred.resolve(checkListSaved);
        });
        return deferred.promise;
    };
    
    function getResourceForCheckList() {
        
        if (!resourceForCheckList) {            
            resourceForCheckList = $resource(HttpContextProvider.getHttpContext() + '/api/checklist/:id', {
                id: '@id'
            }, {
                save: {
                    method: 'PUT'
                }
            });
        }
        
        return resourceForCheckList;
    }
});
