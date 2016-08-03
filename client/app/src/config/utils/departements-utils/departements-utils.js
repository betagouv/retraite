'use strict';

angular.module('SgmapRetraiteConfig').service('DepartementsUtils', function () {

    this.createListDepartementsNumber = function() {
        var departements = [];
        for(var i = 1; i <= 95; i++) {
            if (i == 20) {
                departements.push("2A");
                departements.push("2B");
            } else {
                departements.push((i < 10 ? "0" : "")+i);
            }
        }
        departements.push("971");
        departements.push("972");
        departements.push("973");
        departements.push("974");
        departements.push("976");
        return departements;
    }
    

});