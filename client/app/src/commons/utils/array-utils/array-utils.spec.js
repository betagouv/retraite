'use strict';

describe('ArrayUtils', function () {

    beforeEach(module('SgmapRetraiteCommons'));

    var ArrayUtils;

    beforeEach(inject(function (_ArrayUtils_) {
        ArrayUtils = _ArrayUtils_;
    }));

    describe('replace', function () {
        
        it('should replace at the middle', function () {

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

});
