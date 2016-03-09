'use strict';

angular.module('SgmapRetraiteCommons').service('TemplateLoader', function ($http) {

    this.loadTemplateUrl = function(templateUrl) {
        return $http.get(templateUrl).then(function (res) {
            return res.data || '';
        });
    };

});