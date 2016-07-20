'use strict';

angular.module('SgmapRetraiteConfig').service('DialogLink', function (RetraiteDialog, $q) {

    this.display = function (selectedText) {

        var deferred = $q.defer();
        
        var value = {
            text: selectedText
        };
        
        var options = {
            title: "Insertion d'un lien",
            templateUrl: 'src/config/edit/dialogs/dialog-link/dialog-link.html',
            value: value
        };
        RetraiteDialog.display(options).then(function onSuccess(valueEdited) {
            deferred.resolve(valueEdited);
        }, function onError() {
            deferred.reject();
        });
        
        return deferred.promise;
    };

});