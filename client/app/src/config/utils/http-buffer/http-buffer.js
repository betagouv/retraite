'use strict';

angular.module('SgmapRetraiteConfig').factory('httpBuffer', ['$injector', function ($injector) {
        
    var $http;
    var buffer;

    return {
        storeRequest: function (request) {
            buffer = request;
        },

        retryLastRequest: function () {

            if (!buffer) {
                return;
            }

            function successCallback(response) {
                buffer.deferred.resolve(response);
            }

            function errorCallback(response) {
                buffer.deferred.reject(response);
            }

            $http = $http || $injector.get("$http");
            $http(buffer.config).then(successCallback, errorCallback);
        }
    };
}]);

