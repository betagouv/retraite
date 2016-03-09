'use strict';

angular.module('SgmapRetraiteCommons').service('PromptService', function (prompt) {

    this.promptInformation = function (title, message) {
        return prompt({
            title: title,
            message: message,
            buttons: [{ label:'OK', primary: true }]
        });
    };
    
    this.promptQuestion = function (title, message) {
        return prompt({
            title: title,
            message: message
        });
    };
    
});

