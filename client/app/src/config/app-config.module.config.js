'use strict';

angular.module('SgmapRetraiteConfig').config(function ($urlRouterProvider, $stateProvider, $provide, $httpProvider) {
    
    $urlRouterProvider.otherwise("/configlist");

    var rootPathToViews = 'src/config';

    $stateProvider
        .state ('configlist', {
            url:'/configlist',
            templateUrl: rootPathToViews+'/configlist/configlist.html',
            controller: 'ConfigListCtrl'
        })
        .state ('edit', {
            url:'/edit/{clid}',
            templateUrl: rootPathToViews+'/edit/edit.html',
            controller: 'EditCtrl'
        })
        .state ('test', {
            url:'/test',
            templateUrl: rootPathToViews+'/test/test.html',
            controller: 'TestCtrl'
        })
    
        // Login
    
        .state ('login', {
            url:'/login',
            /*onEnter: function(LoginService) {
                LoginService.setLoginState(true);
            },
            onExit: function(LoginService) {
                LoginService.setLoginState(false);
            },*/
            templateUrl: rootPathToViews+'/login/login.html',
            controller: 'LoginCtrl'
        });

    $provide.decorator('taOptions', function($delegate, taRegisterTool, DialogVariables) {

        taRegisterTool('addVariable', {
            iconclass: "fa fa-th",
            buttontext: "Ajouter une variable...",
            /*action: function (item) {
                if (item.text) {
                    this.$editor().wrapSelection('insertHtml', item.text + '***');
                }
            }*/
            action: function(a,b,c,d){
                var selection = window.getSelection();
                var range = selection.getRangeAt(0);
                DialogVariables.display().then(function(variable) {
                    var textNode = document.createTextNode("{{"+variable+"}}");
                    range.insertNode(textNode);
                    range.setStartAfter(textNode);
                    selection.removeAllRanges();
                    selection.addRange(range);
                });
                //this.$editor().wrapSelection('forecolor', 'red');
                
                
            }
        });

        $delegate.toolbar = [
            ['bold'],['ul', 'ol'],['addVariable']
        ];
        return $delegate;
    });

    $httpProvider.interceptors.push('HttpInterceptor');

});

function startsWith(txt, s) {
    return txt.indexOf(s) == 0;
}
