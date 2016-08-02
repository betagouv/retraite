'use strict';

angular.module('SgmapRetraiteConfig').service('ArrayUtils', function () {

    this.replace = function (array, elementToReplace, newElement) {
        var index = array.indexOf(elementToReplace);
        array.splice(index, 1, newElement);
    };

});