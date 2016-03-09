'use strict';

angular.module('SgmapRetraiteConfig').service('ChecklistPublisher', function ($http, $q) {

    this.publish = function(id) {
        var deferred = $q.defer();
        $http.get('/api/checklistpublish?id='+id).then(function() {
            deferred.resolve();
        });
        return deferred.promise;
    };
});
