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
        if (obj instanceof Object) {
            for(var prop in obj) {
                deletePropertiesInObject(obj[prop], propsToIgnore); 
            }
        }
    }

});