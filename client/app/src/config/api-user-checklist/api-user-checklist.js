'use strict';

angular.module('SgmapRetraiteCommons').service('ApiUserChecklist', function ($q, $http) {
    
    
    this.getChecklist = function(data) {
        
        data = angular.copy(data);
        convert(data);
        var params = addParamsFromObject("", data);
        params = addParam(params, 'published', true);
        var deferred = $q.defer();
        $http.get('/apiuserchecklist/getuserchecklist?'+params).then(function(data) {
            deferred.resolve(data.data);
        });
        return deferred.promise;        
    };
    
    // Privé
    
    var convert = function(data) {
        var regimes = "";
        data.regimes.forEach(function(elt) {
            if (regimes.length > 0) {
                regimes += ",";
            }
            regimes += elt.name;
        });
        data.regimes = regimes;
    };
    
    var addParam = function(params, key, value) {
        if (params.length > 0) {
            params += "&";
        }
        return params+key+"="+value;
    };

    var addParamsFromObject = function(params, obj) {
        for(var prop in obj) {
            if (obj.hasOwnProperty(prop)) {
                var value = obj[prop];
                params = addParam(params, prop, value);
            }
        }
        return params;
    };

});
