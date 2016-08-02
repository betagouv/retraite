'use strict';

describe('TemplateLoader', function () {

    beforeEach(module('SgmapRetraiteConfig'));

    var TemplateLoader, $httpBackend;

    beforeEach(inject(function (_TemplateLoader_, _$httpBackend_) {
        TemplateLoader = _TemplateLoader_;
        $httpBackend = _$httpBackend_;
    }));

    it('should load template URL and return content', function (done) {

        $httpBackend.expectGET('a/b/tempContent.html').respond('contenu');
        
        TemplateLoader.loadTemplateUrl('a/b/tempContent.html').then(function onSuccess(result) {
            expect(result).toEqual('contenu');
            done();
        }, function onError(error) {
            jasmine.fail("Ne devrait pas venir ici");
            done();
        });

        $httpBackend.flush();
    });

    it('should return "" if content is null', function (done) {

        $httpBackend.expectGET('a/b/tempContent.html').respond(200);
        
        TemplateLoader.loadTemplateUrl('a/b/tempContent.html').then(function onSuccess(result) {
            expect(result).toEqual('');
            done();
        }, function onError(error) {
            jasmine.fail("Ne devrait pas venir ici");
            done();
        });

        $httpBackend.flush();
    });

    it('should reject promise if error', function (done) {

        $httpBackend.expectGET('a/b/tempContent.html').respond(404);
        
        TemplateLoader.loadTemplateUrl('a/b/tempContent.html').then(function onSuccess(result) {
            jasmine.fail("Ne devrait pas venir ici");
            done();
        }, function onError(error) {
            expect(error.status).toEqual(404);
            expect(error.data).toEqual(undefined);
            done();
        });

        $httpBackend.flush();
    });

});
