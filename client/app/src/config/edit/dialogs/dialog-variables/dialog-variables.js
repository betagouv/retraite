'use strict';

angular.module('SgmapRetraiteConfig').service('DialogVariables', function (RetraiteDialog, $q) {

    this.display = function () {

        var deferred = $q.defer();
        
        var value = {
            variable: 'regimes_base_hors_alignes'
        };
        
        var options = {
            title: "Insertion d'une variable",
            templateUrl: 'src/config/edit/dialogs/dialog-variables/dialog-variables.html',
            value: value
        };
        RetraiteDialog.display(options).then(function onSuccess(valueEdited) {
            deferred.resolve(valueEdited.variable);
        }, function onError() {
            deferred.reject();
        });
        
        return deferred.promise;
    };

});