'use strict';

angular.module('SgmapRetraiteConfig').controller('AppIdCtrl', function (Wrapper) {

    var host = Wrapper.getLocation().host;
    
    this.appId = startsWith(host, 'recette') ? 'recette' : 
                   startsWith(host, 'localhost') ? 'localhost' : 
                   'prod';
    
});
