'use strict';

angular.module('SgmapRetraiteConfig').factory('ApiCaisseFilter', function ($resource, $q) {
    var resource = $resource('/api/caissefilter/:name/:id', {
        id: '@id'
    }, {
        save: {
            method: 'PUT'
        }
    });
    
    return {
        allForChecklistName: function(name) {
            return resource.query({name:name});
        }
    };
});
