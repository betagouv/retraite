'use strict';

angular.module('SgmapRetraiteConfig').service('CaissesUtils', function (DepartementsUtils, ArrayUtils) {

    this.searchAvailableDepartements = function (caissesDepartementales) {
        var departements = DepartementsUtils.createListDepartementsNumber();
        angular.forEach(caissesDepartementales, function(caisseDepartementale) {
            angular.forEach(caisseDepartementale.departements, function(departement) {
                ArrayUtils.remove(departements, departement);
            });
        });
        return departements;
    };

});