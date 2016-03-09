'use strict';

describe('Welcome exploit page', function () {

    beforeEach(function () {
        var httpBackendMock = function () {
            console.log("111");
            angular.module('httpBackendMock', ['ngMockE2E']).run(function ($httpBackend) {

                console.log("222");
                
                var checkLists = [{
                    "nom": "111",
                    "id": 1
                }, {
                    "nom": "222",
                    "id": 2
                }];                
                $httpBackend.whenGET('/api/checklist').respond(200, checkLists);

                $httpBackend.whenGET(/.*/).passThrough();
            });
        };

        browser.addMockModule('httpBackendMock', httpBackendMock);

        browser.get('http://localhost:9000/local/config.html#/configlist');
    });

    it('should display caisses', function () {
        var caisses = element.all(by.repeater('checklist in checklists'));
        expect(caisses.count()).toEqual(2);
    });


});