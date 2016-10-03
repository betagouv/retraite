'use strict';

// Permet de retailler la hauteur d'une iframe avec la hauteur de son contenu

angular.module('SgmapRetraiteConfig').directive('iframeAutoHeight', [function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.on('load', function() {
                var height = element[0].contentWindow.document.body.scrollHeight + 10;
                console.log("height="+height);
                var iFrameHeight = height + 'px';
                element.css('height', iFrameHeight);
            });
        }
    }
}]);
