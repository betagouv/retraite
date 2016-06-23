'use strict';

angular.module('SgmapRetraiteCommons').service('ObjectUtils', function () {

    this.equalsIgnoringProperties = function (obj1, obj2, propsToIgnore) {
        obj1 = angular.copy(obj1);
        obj2 = angular.copy(obj2);
        if (propsToIgnore) {
            deletePropertiesInObject(obj1, propsToIgnore);
            deletePropertiesInObject(obj2, propsToIgnore);
        }
        return angular.equals(obj1, obj2);
    };
    
    function deletePropertiesInObject(obj, propsToIgnore) {
        propsToIgnore.forEach(function(propToIgnore) {
            delete obj[propToIgnore];  
        });
        for(var prop in obj) {
            if (startsWith(prop, '$')) {
                continue;
            }
            if (obj.hasOwnProperty(prop)) {
                var subobj = obj[prop];
                if (angular.isArray(subobj)) {
                    angular.forEach(subobj, function(item) {
                        deletePropertiesInObject(item, propsToIgnore);  
                    });
                } else if (subobj instanceof Object) {
                    deletePropertiesInObject(subobj, propsToIgnore); 
                }
            }
        }
    }

});