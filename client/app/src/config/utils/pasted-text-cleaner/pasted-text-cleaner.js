'use strict';

angular.module('SgmapRetraiteConfig').service('PastedTextCleaner', function () {

    this.clean = function (oldText, newText) {
        var start = 0;
        while (start < oldText.length && oldText[start] == newText[start]) {
            start++;
        }
        if (isInsideTag(oldText, start)) {
            start = previousStartOfTag(oldText, start);
        }
        var endInOld = start;
        var endInNew = newText.length - (oldText.length - start);
        var diffText = newText.substr(start, endInNew - start);
        return oldText.substr(0,start) + cleanHtmlFormat(diffText) + oldText.substr(endInOld);
    };
    
    function cleanHtmlFormat(text) {
        return  text ? String(text).replace(/<[^>]+>/gm, '').replace(/&nbsp;/gm, ' ') : '';
    }
    
    function isInsideTag(text, position) {
        while (position < text.length) {
            if (text[position] === '<') {
                return false;
            }
            if (text[position] === '>') {
                return true;
            }
            position++;
        }
        return false;
    }
    
    function previousStartOfTag(text, position) {
        while (position >= 0 && text[position] !== '<') {
            position--;
        }
        return position < 0 ? position : position;
    }

    function newEndOfTag(text, position) {
        while (position < text.length && text[position] !== '>') {
            position++;
        }
        return position;
    }

});
