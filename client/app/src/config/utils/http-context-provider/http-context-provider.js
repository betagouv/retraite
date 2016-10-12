'use strict';

angular.module('SgmapRetraiteConfig').service('HttpContextProvider', function (Wrapper) {

    this.getHttpContext = function() {
        var pathname = Wrapper.getLocation().pathname;
        var endIndex = pathname.indexOf('/config/') == -1 ? pathname.indexOf('/local/') : pathname.indexOf('/config/');        
        return pathname.substring(0, endIndex);
    };
    
});