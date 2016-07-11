'use strict';

angular.module('SgmapRetraiteCommons').service('SyntaxAnalyzer', function () {

    this.isSyntaxError = function (text) {
        if (!text) return false;
        return isMustacheVarsSyntaxError(text);
    };

    function isMustacheVarsSyntaxError(text) {
        var begin = text.indexOf('{{');
        var end = text.indexOf('}}');
        if (begin == -1 && end == -1) {
            return false;
        }
        if (end == -1) {
            return true; 
        }
        if (begin < end) {
            var beginNext = text.indexOf('{{', begin+2);
            if (beginNext != -1 && beginNext < end) {
                return true;
            }
            var next = text.substr(end+1);
            return isMustacheVarsSyntaxError(next);
        }
        return false;
    }
});