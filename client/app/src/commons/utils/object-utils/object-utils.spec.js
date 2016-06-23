'use strict';

describe('ObjectUtils', function () {

    beforeEach(module('SgmapRetraiteCommons'));

    var ObjectUtils;

    beforeEach(inject(function (_ObjectUtils_) {
        ObjectUtils = _ObjectUtils_;
    }));

    describe('equalsIgnoringProperties', function () {
        
        it('should compare simple objects with all properties', function () {

            var obj1 = {id: 1, nom: "nom 1"};
            var obj2 = {id: 1, nom: "nom 1"};
            var obj1WithDiff = {id: 1, nom: "nom 1 diff"};
            
            expect(ObjectUtils.equalsIgnoringProperties(obj1, obj2)).toBeTruthy();
            expect(ObjectUtils.equalsIgnoringProperties(obj1, obj1WithDiff)).toBeFalsy();
        });
        
        it('should compare ignoring properties', function () {

            var checklist1 = {
                nom: "CNAV",
                published: false,
                modifiedButNotPublished: true,
                // Pour tester avec les tableaux
                chapitres: [
                    {
                        id: 1,
                        titre: 'aaa',
                        type: 'type 1'
                    },{
                        id: 2,
                        titre: 'bbb',
                        type: 'type 1'
                    }
                ],
                // Pour reproduire le problème de la Resource quelque part dans l'arborescence
                // qui contient une $promise dont la valeur (dans $$state avec Angular)
                // pointe sur l'objet conteneur (= boucle)
                $promise: {
                },
                // Ignorer les propriétés suivantes
                type: "cnav",
                id: 1,
                propThatCanBeNull: undefined
            };    
            checklist1.$promise.value = checklist1;
            var checklist2 = {
                nom: "CNAV",
                published: false,
                modifiedButNotPublished: true,
                chapitres: [
                    {
                        id: 1,
                        titre: 'aaa',
                        type: 'type 2' // Diff !
                    },{
                        id: 2,
                        titre: 'bbb',
                        type: 'type 3' // Diff !
                    }
                ],
                $promise: {
                },
                // Ignorer les propriétés suivantes
                type: "msa",
                id: 2
            };           
            checklist2.$promise.value = checklist2;
            var checklist1WithDiff = {
                nom: "CNAV",
                published: false,
                modifiedButNotPublished: false, // Diff !
                chapitres: [
                    {
                        id: 1,
                        titre: 'aaa xxx', // Diff !
                        type: 'type 2' // Diff !
                    },{
                        id: 2,
                        titre: 'bbb',
                        type: 'type 3' // Diff !
                    }
                ],
                $promise: {
                },
                // Ignorer les propriétés suivantes
                type: "msa",
                id: 1
            };
            checklist1WithDiff.$promise.value = checklist1WithDiff;
            
            // On vérifie que c'est différent avec toutes les propriétés
            expect(ObjectUtils.equalsIgnoringProperties(checklist1, checklist2)).toBeFalsy();
            expect(ObjectUtils.equalsIgnoringProperties(checklist1, checklist1WithDiff)).toBeFalsy();
            
            // On vérifie que c'est "equal" en ignorant certaines propriétés
            expect(ObjectUtils.equalsIgnoringProperties(checklist1, checklist2, ['type','id'])).toBeTruthy();
            expect(ObjectUtils.equalsIgnoringProperties(checklist1, checklist1WithDiff, ['type','id'])).toBeFalsy();
        });
        
        it('should compare ignoring properties on deep objects', function () {

            var checklist1 = {
                nom: "CNAV",
                published: false,
                modifiedButNotPublished: false,
                type: "cnav",
                chapitres: [{
                    titre: "yyyy",
                    closedInEdition: false,
                    texteActions: "bla bla bla</p>",
                    id: 1
                }, {
                    titre: "xxx",
                    closedInEdition: false,
                    texteActions: "blo blo blo",
                    id: 2
                }],
                id: 1
            };           
            var checklist2 = {
                nom: "CNAV",
                published: false,
                modifiedButNotPublished: false,
                type: "cnav",
                chapitres: [{
                    titre: "yyyy modifié",// La différence 1/2 est là !
                    closedInEdition: false,
                    texteActions: "bla bla bla</p>",
                    id: 1
                }, {
                    titre: "xxx",
                    closedInEdition: true,// La différence 2/2 est là !
                    texteActions: "blo blo blo",
                    id: 2
                }],
                id: 1
            };           
            expect(ObjectUtils.equalsIgnoringProperties(checklist1, checklist2)).toBeFalsy();
            expect(ObjectUtils.equalsIgnoringProperties(checklist1, checklist2, ['titre','closedInEdition'])).toBeTruthy();
        });
        
    });

});
