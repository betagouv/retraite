'use strict';

describe('EditConditionValidator', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    var EditConditionValidator;

    beforeEach(inject(function (_EditConditionValidator_, _$httpBackend_) {
        EditConditionValidator = _EditConditionValidator_;
    }));

    describe('delai', function () {
        
        it('should return undefined if valid', function () {

            var error = EditConditionValidator.isDelaiValide({
                props: {
                    plusOuMoins:"PLUS",
                    nombre:"4",
                    unite:"ANNEES"
                }
            });
            expect(error).toBeUndefined();
            
        });

        it('should return error if no plusOuMoins', function () {

            var error = EditConditionValidator.isDelaiValide({
                props: {
                    //plusOuMoins:"PLUS",
                    nombre:"4",
                    unite:"ANNEES"
                }
            });
            expect(error).toEqual("Vous devez saisir toutes les valeurs");
            
        });

        it('should return error if no nombre', function () {

            var error = EditConditionValidator.isDelaiValide({
                props: {
                    plusOuMoins:"PLUS",
                    //nombre:"4",
                    unite:"ANNEES"
                }
            });
            expect(error).toEqual("Vous devez saisir toutes les valeurs");
            
        });

        it('should return error if no nombre', function () {

            var error = EditConditionValidator.isDelaiValide({
                props: {
                    plusOuMoins:"PLUS",
                    nombre:"4"
                    //unite:"ANNEES"
                }
            });
            expect(error).toEqual("Vous devez saisir toutes les valeurs");
            
        });

    });
});
