'use strict';

describe('ArrayUtils', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    beforeEach(module(function($provide) {
        
        // Subterfuge pour éviter l'erreur suivante apparemment lié au chargement de $state dans les tests :
        // Error: Unexpected request: GET src/config/configlist/configlist.html
        
        $provide.service('$state', function() {
            this.go = function(newState) {};
            this.reload = function() {};
        });
    }));

    var ArrayUtils;

    beforeEach(inject(function (_ArrayUtils_) {
        ArrayUtils = _ArrayUtils_;
    }));

    describe('replace', function () {
        
        it('should replace item', function () {

            var obj1 = {id: 1, nom: "nom 1"};
            var obj2 = {id: 2, nom: "nom 2"};
            var obj3 = {id: 3, nom: "nom 3"};
            var array = [ obj1, obj2, obj3 ];
            
            var obj2bis = {id: 22, nom: "nom 2 bis"};
            
            ArrayUtils.replace(array, obj2, obj2bis);

            var expectedArray = [ obj1, obj2bis, obj3 ];
            expect(angular.equals(array, expectedArray)).toBeTruthy();
        });
        
    });

    describe('remove', function () {
        
        it('should remove item', function () {

            var obj1 = {id: 1, nom: "nom 1"};
            var obj2 = {id: 2, nom: "nom 2"};
            var obj3 = {id: 3, nom: "nom 3"};

            var array = [ obj1, obj2, obj3 ];
            ArrayUtils.remove(array, obj1);
            expect(angular.equals(array, [ obj2, obj3 ])).toBeTruthy();

            var array = [ obj1, obj2, obj3 ];
            ArrayUtils.remove(array, obj2);
            expect(angular.equals(array, [ obj1, obj3 ])).toBeTruthy();

            var array = [ obj1, obj2, obj3 ];
            ArrayUtils.remove(array, obj3);
            expect(angular.equals(array, [ obj1, obj2 ])).toBeTruthy();

            var array = ['47', '48', '971', '972', '973', '974'];
            ArrayUtils.remove(array, '971');
            expect(array).toEqual([ '47', '48', '972', '973', '974' ]);
            ArrayUtils.remove(array, '972');
            expect(array).toEqual([ '47', '48', '973', '974' ]);
            ArrayUtils.remove(array, '973');
            expect(array).toEqual([ '47', '48', '974' ]);
            ArrayUtils.remove(array, '974');
            expect(array).toEqual([ '47', '48', ]);

        });
        
        it('should do nothing if item not found', function () {
            var array = ['48', '972', '973'];
            ArrayUtils.remove(array, '974');
            expect(array).toEqual(['48', '972', '973']);
        });
    });

});
