'use strict';

angular.module('SgmapRetraiteCommons').service('SyntaxAnalyzer', function () {

    this.isSyntaxError = function (text) {
        if (!text) return false;
        return isMustacheVarsSyntaxError(text) || isBracketsLinksSyntaxError(text);
    };

    function isMustacheVarsSyntaxError(text) {
        return isSyntaxError(text, '{{', '}}');
    }
    
    function isBracketsLinksSyntaxError(text) {
        return isSyntaxError(text, '[[', ']]');
    }
    
    function isSyntaxError(text, beginDelimiter, endDelimiter) {
        var begin = text.indexOf(beginDelimiter);
        var end = text.indexOf(endDelimiter);
        if (begin == -1 && end == -1) { 
            return false;
        }
        if (end == -1) {
            return true; 
        }
        if (begin < end) {
            var beginNext = text.indexOf(beginDelimiter, begin+2);
            if (beginNext != -1 && beginNext < end) {
                return true;
            }
            var next = text.substr(end+1);
            return isSyntaxError(next, beginDelimiter, endDelimiter);
        }
        return false;
    }
    
});