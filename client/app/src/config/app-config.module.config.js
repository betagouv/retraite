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
        .state ('caisses', {
            url:'/caisses/{name}',
            templateUrl: rootPathToViews+'/caisses/caisses.html',
            controller: 'CaissesCtrl'
        })
        .state ('caisses.edit', {
            url:'/edit/{id}',
            templateUrl: rootPathToViews+'/edit-caisse/edit-caisse.html',
            controller: 'EditCaisseCtrl'
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

    $provide.decorator('taOptions', function($delegate, taRegisterTool, DialogVariables, DialogLink) {

        taRegisterTool('addVariable', {
            iconclass: "fa fa-th",
            buttontext: "Ajouter une variable...",
            action: function(){
                var selection = window.getSelection();
                var range = selection.getRangeAt(0);
                DialogVariables.display().then(function(variable) {
                    var textNode = document.createTextNode("{{"+variable+"}}");
                    range.insertNode(textNode);
                    range.setStartAfter(textNode);
                    selection.removeAllRanges();
                    selection.addRange(range);
                });
            }
        });

        taRegisterTool('addLink', {
            iconclass: "fa fa-th",
            buttontext: "Ajouter un lien...",
            action: function(){
                var selection = window.getSelection();
                var range = selection.getRangeAt(0);
                var selectedText = selection.toString();
                DialogLink.display(selectedText).then(function(result) {
                    range.deleteContents();
                    var textNode = document.createTextNode("[["+result.text+" "+result.url+"]]");
                    range.insertNode(textNode);
                    range.setStartAfter(textNode);
                    selection.removeAllRanges();
                    selection.addRange(range);
                });
            }
        });

        $delegate.toolbar = [
            ['bold'],['ul', 'ol'],['addVariable', 'addLink']
        ];
        return $delegate;
    });

    $httpProvider.interceptors.push('HttpInterceptor');

});

function startsWith(txt, s) {
    return txt.indexOf(s) == 0;
}
