'use strict';

angular.module('SgmapRetraiteConfig').service('DialogDelai', function (RetraiteDialog) {

    this.displayAndProcess = function (chapitre, type) {

        var defaultValue = {
            type: 'desque',
            min: 1,
            max: 1,
            unite: 'MOIS'
        };
        
        var valueToEdit = (type === "SA" ? chapitre.delaiSA : chapitre.delai) || defaultValue;
        
        var options = {
            title: "Configuration du délai",
            templateUrl: 'src/config/edit/dialogs/dialog-delai/dialog-delai.html',
            value: valueToEdit
        };
        RetraiteDialog.display(options).then(function onSuccess(delaiEdited) {
            if (type === "SA") {                
                chapitre.delaiSA = delaiEdited;
            } else {
                chapitre.delai = delaiEdited;
            }
        });
    };

});