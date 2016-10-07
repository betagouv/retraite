'use strict';

describe('HttpContextProvider', function () {
    
    beforeEach(module('SgmapRetraiteConfig'));
    
    var HttpContextProvider, Wrapper;
    
    beforeEach(inject(function (_HttpContextProvider_, _Wrapper_) {

        HttpContextProvider = _HttpContextProvider_;
        Wrapper = _Wrapper_;
                
    }));
    
    describe('getHttpContext', function () {

        it('should provide the http context', function () {
                
            spyOn(Wrapper, 'getLocation').and.returnValue({pathname: "/retraite/config/config.html"});
            
            var httpContext = HttpContextProvider.getHttpContext();
            
            expect(httpContext).toEqual('/retraite');
        });
        
        it('should provide the http context from play server', function () {
                
            spyOn(Wrapper, 'getLocation').and.returnValue({pathname: "/config/config.html"});
            
            var httpContext = HttpContextProvider.getHttpContext();
            
            expect(httpContext).toEqual('');
        });
        
        it('should provide the http context from local', function () {
                
            spyOn(Wrapper, 'getLocation').and.returnValue({pathname: "/local/config.html"});
            
            var httpContext = HttpContextProvider.getHttpContext();
            
            expect(httpContext).toEqual('');
        });

    });

});
