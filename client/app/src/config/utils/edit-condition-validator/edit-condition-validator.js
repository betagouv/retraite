'use strict';

angular.module('SgmapRetraiteConfig').service('EditConditionValidator', function ($http) {

    this.isDelaiValide = function(condition) {
        if (condition.props.plusOuMoins && condition.props.nombre && condition.props.unite) {
            return;
        }
        return "Vous devez saisir toutes les valeurs";
    };
});