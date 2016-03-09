'use strict';

describe('httpBuffer', function () {

    beforeEach(module('SgmapRetraiteCommons'));

    var httpBuffer, $httpBackend;

    beforeEach(inject(function (_httpBuffer_, _$httpBackend_) {
        httpBuffer = _httpBuffer_;
        $httpBackend = _$httpBackend_;
    }));

    it('should store request, do retry and call resolve if success', function () {

        var mockDeferred = {
            resolveData: '',
            resolve: function(response) {
                this.resolveData = response.data;
            }
        };

        var mockConfig = {
            method: 'get',
            url: '/toto',
            data: "data"
        };

        var request = {
            deferred: mockDeferred,
            config: mockConfig
        };

        $httpBackend.expectGET('/toto').respond('result');

        httpBuffer.storeRequest(request);

        httpBuffer.retryLastRequest();

        $httpBackend.flush();
        expect(mockDeferred.resolveData).toEqual('result');
    });

    it('should store request, do retry and call reject if error', function () {

        var mockDeferred = {
            rejectData: '',
            reject: function(response) {
                this.rejectData = response.data;
            }
        };

        var mockConfig = {
            method: 'get',
            url: '/toto',
            data: "data"
        };

        var request = {
            deferred: mockDeferred,
            config: mockConfig
        };

        $httpBackend.expectGET('/toto').respond(500, 'Internal error');

        httpBuffer.storeRequest(request);

        httpBuffer.retryLastRequest();

        $httpBackend.flush();
        expect(mockDeferred.rejectData).toEqual('Internal error');
    });
             
    it('should do nothing if no previous request', function () {

        httpBuffer.retryLastRequest();

        // OK if no error 
    });
             
});
