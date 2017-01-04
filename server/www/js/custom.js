"use strict";

 angular.module('SgmapRetraiteCommon', [])

.constant('ENV', {apiEndPoint:''})

.constant('ENV_MOBILE', {apiEndPoint:'https://monserver/'})

;
'use strict';

angular.module('SgmapRetraiteConfig', [
    'ngAnimate',
    'ngDialog',
    'ngCookies',
    'ngResource',
    'ui.bootstrap',
    'ui.router',
    'toaster',
    'ngDialog',
    'cgPrompt',
    'textAngular',
    'xeditable',
    'angularjs-dropdown-multiselect'
]);


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

'use strict';

angular.module('SgmapRetraiteConfig').run(function(editableOptions) {
    editableOptions.theme = 'bs3';
});
'use strict';

angular.module('SgmapRetraiteConfig').controller('AppIdCtrl', function (Wrapper, $rootScope, $http) {

    var host = Wrapper.getLocation().host;
    
    this.appId = startsWith(host, 'recette') ? 'recette' : 
                   startsWith(host, 'localhost') ? 'localhost' : 
                   'prod';

    //Condition temporaire pour permettre fonctionnement sur environnement SGMAP, à supprimer
    if (Wrapper.getLocation().pathname && Wrapper.getLocation().pathname.indexOf('passretraite') != -1) {
	    $http({
	        method : 'GET',
	        url : '/passretraite-config-rs/api/ng-configuration'
	    })
	    .success(function(data) {
	    	//Ajout dans le rootScope
	    	for (var prop in data) {
	    		$rootScope[prop] = data[prop];
	    	}
	    })
    }
    /*else {
    	$rootScope.NG_URL_PASSRETRAITE_WS = 'http://localhost:8090/passretraite-ihm-1.0.0-SNAPSHOT';  
    }*/
});

'use strict';

angular.module('SgmapRetraiteConfig').controller('CaissesCtrl', function ($rootScope, $scope, $state, $stateParams, ApiCaisseFilter, PromptService, WsCaisseDepartement, RetraiteDialog, CaissesUtils) {

    // Actions
    
    $scope.confirmeDepartementDelete = function(caisse, departement) {
        PromptService.promptQuestion("Confirmation", "Etes-vous sûr de vouloir supprimer ce département ?").then(function() {
            if (caisse.departements.length == 1) {
                PromptService.promptQuestion("Confirmation", "Attention, sans département, cette caisse sera supprimée. Etes-vous sûr de vouloir continuer ?").then(function() {
                    deleteDepartement(caisse.id, departement);
                });
            } else {
                deleteDepartement(caisse.id, departement);
            }
        });
    };
    
    $scope.addDepartement = function(checklistName, caisseId) {
        var promise = callDialogToAddDepartement("Ajouter un département");
        if (promise) {
            promise.then(function(departement) {
                WsCaisseDepartement.addDepartement(checklistName, caisseId, departement).then(function() {
                    $state.reload();
                });
            });
        }
    };
    
    $scope.addCaisse = function(checklistName) {
        var promise = callDialogToAddDepartement("Ajouter une caisse");
        if (promise) {
            promise.then(function(departement) {
                WsCaisseDepartement.addCaisse(checklistName, departement).then(function(result) {
                    $state.reload();
                    PromptService.promptInformation("Information", "La caisse a été créée avec un nom par défaut, vous pouvez la retrouver dans la liste de gauche, probablement en fin de liste. Vous pouvez alors cliquer dessus pour modifier ses informations.");
                });
            });
        }
    };
    
    // Evénements
    
    $rootScope.$on('caisseSaved', function() {
        loadAllCaissesForChecklistName();
    });
    
    // Données
    
    function loadAllCaissesForChecklistName() {
        ApiCaisseFilter.allForChecklistName($scope.name).$promise.then(function(caisses) {
            $scope.caisses = caisses;
            $scope.availableDepartements = CaissesUtils.searchAvailableDepartements($scope.caisses);
        });
    };

    $scope.name = $stateParams.name;
    loadAllCaissesForChecklistName();
    
    // Privé
    
    function callDialogToAddDepartement(dialogTitle) {
        
        var availableDepartements = CaissesUtils.searchAvailableDepartements($scope.caisses);
        
        if (availableDepartements.length == 0) {
            PromptService.promptInformation("Attention", "Désolé, aucun département disponible pour être ajouté. Vous devez d'abord supprimer un département de la caisse à laquelle il est affecté en cliquant sur son numéro.");
            return;
        }
        
        return RetraiteDialog.display({
            title: dialogTitle,
            templateUrl: 'src/config/caisses/dialogs/add-departement/add-departement.html',
            value: availableDepartements[0],
            data: {
                departements: availableDepartements
            }
        });
    };
    
    function deleteDepartement(caisseId, departement) {
        WsCaisseDepartement.deleteDepartement(caisseId, departement).then(function() {
            $state.reload();
        });
    };
});


'use strict';

angular.module('SgmapRetraiteConfig').controller('ConfigListCtrl', function ($scope, $stateParams, CheckList, $state) {
    
    $scope.checklists = CheckList.all();

});


'use strict';

angular.module('SgmapRetraiteConfig').factory('ApiCaisse', function ($resource, $q, HttpContextProvider) {
    var resource = $resource(HttpContextProvider.getHttpContext() + '/api/caisse/:id', {
        id: '@id'
    }, {
        save: {
            method: 'PUT'
        }
    });
    
    return {
        all: function() {
            return resource.query();
        },
        get: function(id) {
            return resource.get({id:id});
        },
        save: function(caisse) {
            var deferred = $q.defer();
            resource.save(caisse, function onSuccess(caisseSaved) {
                deferred.resolve(caisseSaved);
            });
            return deferred.promise;
        }
    };
});

'use strict';

angular.module('SgmapRetraiteConfig').factory('ApiCaisseFilter', function ($resource, $q, HttpContextProvider) {
    var resource = $resource(HttpContextProvider.getHttpContext() + '/api/caissefilter/:name/:id', {
        id: '@id'
    }, {
        save: {
            method: 'PUT'
        }
    });
    
    return {
        allForChecklistName: function(name) {
            return resource.query({name:name});
        }
    };
});

'use strict';

angular.module('SgmapRetraiteConfig').service('ApiRegimes', function ($q, $http, HttpContextProvider) {
 
    this.getRegimes = function() {
        var deferred = $q.defer();
        $http.get(HttpContextProvider.getHttpContext() + '/apirestregimes/getregimes')
            .then(function(data) {
                deferred.resolve(data.data);
            }, function onError(error) {
                deferred.reject(error);
            });
        return deferred.promise;        
    };
    
});

'use strict';

angular.module('SgmapRetraiteConfig').service('ApiUserChecklist', function ($q, $http, HttpContextProvider) {
    
    this.getChecklistUrl = function(data, published, full) {
        data = angular.copy(data);
        convert(data);
        var params = addParamsFromObject("", data);
        params = addParam(params, 'published', !!published);
        if (full == true) {
            params = addParam(params, 'full', true);            
        }
        return HttpContextProvider.getHttpContext() + '/apiuserchecklist/getuserchecklist?'+params;
    };
    
    this.getChecklist = function(data) {
        var deferred = $q.defer();
        var url = this.getChecklistUrl(data);
        $http.get(url).then(function(data) {
            deferred.resolve(data.data);
        });
        return deferred.promise;        
    };
    
    // Privé
    
    var convert = function(data) {
        var regimes = "";
        data.regimes.forEach(function(elt) {
            if (regimes.length > 0) {
                regimes += ",";
            }
            regimes += elt.name;
        });
        data.regimes = regimes;
    };
    
    var addParam = function(params, key, value) {
        if (params.length > 0) {
            params += "&";
        }
        return params+key+"="+value;
    };

    var addParamsFromObject = function(params, obj) {
        for(var prop in obj) {
            if (obj.hasOwnProperty(prop)) {
                var value = obj[prop];
                params = addParam(params, prop, value);
            }
        }
        return params;
    };

});

'use strict';

angular.module('SgmapRetraiteConfig').service('CheckList', function ($resource, $q, HttpContextProvider) {
    
    var resourceForCheckList;
    
    this.all = function() {
        return getResourceForCheckList().query();
    };
    
    this.get = function(id) {
        return getResourceForCheckList().get({id:id});
    };
        
    this.save = function(checkList) {
        var deferred = $q.defer();
        getResourceForCheckList().save(checkList, function onSuccess(checkListSaved) {
            deferred.resolve(checkListSaved);
        });
        return deferred.promise;
    };
    
    function getResourceForCheckList() {
        
        if (!resourceForCheckList) {            
            resourceForCheckList = $resource(HttpContextProvider.getHttpContext() + '/api/checklist/:id', {
                id: '@id'
            }, {
                save: {
                    method: 'PUT'
                }
            });
        }
        
        return resourceForCheckList;
    }
});

'use strict';

angular.module('SgmapRetraiteConfig').service('WsCaisseDepartement', function ($q, $http, HttpContextProvider) {
 
    this.deleteDepartement = function(caisseId, departement) {
        return callWs('deletedepartement', caisseId, departement);
    };
    
    this.addDepartement = function(checklistName, caisseId, departement) {
        return callWs('adddepartement', caisseId, departement, checklistName);
    };
    
    this.addCaisse = function(checklistName, departement) {
        return callWs('addcaisse', null, departement, checklistName);
    };
    
    // Privé
    
    function callWs(operation, caisseId, departement, checklistName) {
        var url = HttpContextProvider.getHttpContext() + '/ws/caissedepartement/'+operation+'?';
        if (checklistName) {
            url += 'checklistName='+checklistName+'&';
        }
        if (caisseId) {
            url += 'caisseId='+caisseId+'&';
        }
        url += 'departement='+departement;
        
        var deferred = $q.defer();
        $http.get(url)
            .then(function(respond) {
                deferred.resolve(respond.data);
            }, function onError(error) {
                deferred.reject(error);
            });
        return deferred.promise;
    }
});

'use strict';

// Permet de retailler la hauteur d'une iframe avec la hauteur de son contenu

angular.module('SgmapRetraiteConfig').directive('iframeAutoHeight', [function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.on('load', function() {
                var height = element[0].contentWindow.document.body.scrollHeight + 10;
                console.log("height="+height);
                var iFrameHeight = height + 'px';
                element.css('height', iFrameHeight);
            });
        }
    }
}]);

'use strict';

angular.module('SgmapRetraiteConfig').controller('EditCaisseCtrl', function ($rootScope, $scope, $stateParams, ApiCaisse, $document) {

    // Actions
    
    $scope.save = function() {
        ApiCaisse.save($scope.caisse);
        $rootScope.$broadcast('caisseSaved');
    };
    
    // Données

    ApiCaisse.get($stateParams.id).$promise.then(function(caisse) {
        $scope.caisse = caisse;
    });
    
});


'use strict';

angular.module('SgmapRetraiteConfig').service('DialogDelai', function (RetraiteDialog) {

    this.displayAndProcess = function (chapitre, type) {

        var defaultValue = {
            type: 'desque',
            min: 1,
            max: 1,
            unite: 'MOIS'
        };
        
        var valueToEdit = (type === "SA" ? chapitre.delaiSA : chapitre.delai) || defaultValue;
        
        var options = {
            title: "Configuration du délai",
            templateUrl: 'src/config/edit/dialogs/dialog-delai/dialog-delai.html',
            value: valueToEdit
        };
        RetraiteDialog.display(options).then(function onSuccess(delaiEdited) {
            if (type === "SA") {                
                chapitre.delaiSA = delaiEdited;
            } else {
                chapitre.delai = delaiEdited;
            }
        });
    };

});
'use strict';

angular.module('SgmapRetraiteConfig').service('DialogLink', function (RetraiteDialog, $q) {

    this.display = function (selectedText) {

        var deferred = $q.defer();
        
        var value = {
            text: selectedText
        };
        
        var options = {
            title: "Insertion d'un lien",
            templateUrl: 'src/config/edit/dialogs/dialog-link/dialog-link.html',
            value: value
        };
        RetraiteDialog.display(options).then(function onSuccess(valueEdited) {
            deferred.resolve(valueEdited);
        }, function onError() {
            deferred.reject();
        });
        
        return deferred.promise;
    };

});
'use strict';

angular.module('SgmapRetraiteConfig').service('DialogVariables', function (RetraiteDialog, $q) {

    this.display = function () {

        var deferred = $q.defer();
        
        var value = {
            variable: 'regimes_base_hors_alignes'
        };
        
        var options = {
            title: "Insertion d'une variable",
            templateUrl: 'src/config/edit/dialogs/dialog-variables/dialog-variables.html',
            value: value
        };
        RetraiteDialog.display(options).then(function onSuccess(valueEdited) {
            deferred.resolve(valueEdited.variable);
        }, function onError() {
            deferred.reject();
        });
        
        return deferred.promise;
    };

});
'use strict';

angular.module('SgmapRetraiteConfig').controller('EditCtrl', 
            function ($scope, $state, $stateParams, CheckList, RetraiteDialog, ArrayUtils, EditConditionValidator, $window, PromptService, DialogDelai, RetraiteToaster, ChecklistPublisher, PastedTextCleaner, ObjectUtils, SyntaxAnalyzer, HttpContextProvider) {

    // Actions
    
    $scope.save = function() {
        CheckList.save($scope.checklist).then(function(checklist) {
            storeChecklist(checklist, false);
            RetraiteToaster.popSuccess("Enregistrée avec succès !");
        });
    };
    
    $scope.publish = function() {
        PromptService.promptQuestion("Confirmation", "Etes-vous sûr de vouloir publier cette checklist ?").then(function() {
            CheckList.save($scope.checklist).then(function(checklist) {
                storeChecklist(checklist, false);
                ChecklistPublisher.publish($scope.checklist.id).then(function() {
                    RetraiteToaster.popSuccess("Enregistrée et publiée avec succès !");
                    getChecklistToEdit();
                });
            });
        });
    };
    
    $scope.openDocumentation = function(published) {
        $window.open(HttpContextProvider.getHttpContext() + '/application/generateDoc?checklistNom='+$scope.checklist.nom+'&published='+published, '_blank');
    };
    
    $scope.openAllChapitres = function() {
        $scope.checklist.chapitres.forEach(function (chapitre) {
            chapitre.closedInEdition = false;
        });
    };
    
    $scope.closeAllChapitres = function() {
        $scope.checklist.chapitres.forEach(function (chapitre) {
            chapitre.closedInEdition = true;
        });
    };
    
    $scope.addChapitre = function() {
        $scope.checklist.chapitres.push({ titre: "Nouveau chapitre" });
    };
    
    $scope.orderUp = function(chapitre) {
        var chapitres = $scope.checklist.chapitres;
        for(var i = 0; i < chapitres.length; i++) {
            if (chapitres[i] == chapitre) {
                chapitres.splice(i, 1);
                chapitres.splice(i-1, 0, chapitre);
                return;
            }
        }
    };

    $scope.orderDown = function(chapitre) {
        var chapitres = $scope.checklist.chapitres;
        for(var i = 0; i < chapitres.length; i++) {
            if (chapitres[i] == chapitre) {
                chapitres.splice(i, 1);
                chapitres.splice(i+1, 0, chapitre);
                return;
            }
        }
    };
    
    $scope.duplicateChapitre = function(chapitre) {
        
        function removeAllIdsInChapitre(newChapitre) {
            delete newChapitre.id;
            angular.forEach(newChapitre.conditions, function(condition) {
                delete condition.id;
            }); 
        }
        
        var chapitres = $scope.checklist.chapitres;
        for(var i = 0; i < chapitres.length; i++) {
            if (chapitres[i] == chapitre) {
                var newChapitre = angular.copy(chapitre);
                removeAllIdsInChapitre(newChapitre);
                newChapitre.titre += " - Copie";
                chapitres.splice(i+1, 0, newChapitre);
                return;
            }
        }
    };

    $scope.removeChapitre = function(chapitre) {
        
        PromptService.promptQuestion("Confirmation", "Etes-vous sûr de vouloir supprimer ce chapitre ?").then(function() {
            var chapitres = $scope.checklist.chapitres;
            for(var i = 0; i < chapitres.length; i++) {
                if (chapitres[i] == chapitre) {
                    chapitres.splice(i, 1);
                    return;
                }
            }
        });

    };

    $scope.editCondition = function(chapitre, condition) {
        if (condition.props && condition.props.type == 'delai') {
            editConditionDelai(chapitre, condition, false);
        }
    };
    
    // Ajout conditions : type simple
    
    $scope.addConditionType = function(chapitre, type) {
        var condition = {
            props: {
                type: type
            }
        };
        chapitre.conditions = chapitre.conditions || [];
        chapitre.conditions.push(condition);
    };
    
    // Ajout conditions : délai
    
    $scope.addConditionDelai = function(chapitre) {
        editConditionDelai(chapitre, {props:{}}, true);
    };
    
    var editConditionDelai = function(chapitre, condition, adding) {
        var options = {
            title: "Critère : délai",
            templateUrl: 'src/config/edit/dialogs/editConditionDelai.html',
            value: condition,
            canValidate: function(condition) {
                return EditConditionValidator.isDelaiValide(condition);
            }
        };
        RetraiteDialog.display(options).then(function (conditionEdited) {
            conditionEdited.props.type = 'delai';
            chapitre.conditions = chapitre.conditions || [];
            if (adding) {
                chapitre.conditions.push(conditionEdited);
            } else {
                ArrayUtils.replace(chapitre.conditions, condition, conditionEdited);
            }
        });
    };
    
    // Ajout conditions : régime xxx détecté
    
    $scope.addConditionRegimeDetecte = function(chapitre, regime) {
        var conditionRegimeDetecte = {
            props: {
                type:'regimeDetecte',
                regime: regime
            }
        };
        chapitre.conditions = chapitre.conditions || [];
        chapitre.conditions.push(conditionRegimeDetecte);
    };
    
    // Ajout conditions : Statut
    
    $scope.addConditionStatut = function(chapitre, statut) {
        var conditionNsa = {
            props: {
                type:"statut",
                statut: statut
            }
        };
        chapitre.conditions = chapitre.conditions || [];
        chapitre.conditions.push(conditionNsa);
    };
    
    // Suppression d'une condition
    
    $scope.removeCondition = function(chapitre, condition) {
        PromptService.promptQuestion("Confirmation","Etes-vous sûr de vouloir supprimer cette condition ?").then(function() {
            var index = chapitre.conditions.indexOf(condition);
            chapitre.conditions.splice(index, 1);
        });
    };
    
    // Edition des delais
    
    $scope.editDelai = function(chapitre) {
        DialogDelai.displayAndProcess(chapitre);
    };
    
    $scope.editDelaiSA = function(chapitre) {
        DialogDelai.displayAndProcess(chapitre, "SA");
    };
    
    // Conversions
    
    $scope.conditionToHumanStr = function(condition) {
        if (condition.props.type === 'delai') {
            return "Délai "
                +(condition.props.plusOuMoins === 'MOINS' ? '< ' : '> ')
                +condition.props.nombre+' '
                +(condition.props.unite === 'MOIS' ? 'mois' : 'années');
        }
        if (condition.props.type === 'regimeDetecte') {
            if (condition.props.regime === 'agirc-arrco') {
                return "Détecté : AGIRC-ARRCO";
            }
            if (condition.props.regime === 'regimes-base-hors-alignés') {
                return "Détecté : Régimes de base hors alignés";
            }
            if (condition.props.regime === 'regimes-complémentaires-hors-agirc-arrco') {
                return "Détecté : Régimes compl. hors AGIRC-ARRCO";
            }
            if (condition.props.regime === 'regimes-hors-alignés-et-hors-agirc-arrco') {
                return "Détecté : Régimes hors alignés et hors AGIRC-ARRCO";
            }
            if (condition.props.regime === 'regimes-hors-alignes-ou-regimes-compl') {
                return "Détecté : Régimes hors alignés ou régimes compl.";
            }
            return "!! "+JSON.stringify(condition.props).replace(/\"/g, "'")+" !!";
        }
        if (condition.props.type === 'statut') {
            if (condition.props.statut === 'nsa') {
                return "NSA";
            }
            if (condition.props.statut === 'sa') {
                return "SA";
            }
            if (condition.props.statut === 'conjoint-collaborateur') {
                return "Coinjoint-collaborateur";
            }
            if (condition.props.statut === 'chef-entreprise') {
                return "Chef d'entreprise";
            }
            return "!! "+JSON.stringify(condition.props).replace(/\"/g, "'")+" !!";
        }
        if (condition.props.type === 'carriere-longue-oui') {
            return "Carrière longue : Oui (l'assuré a une attestation)";
        }
        if (condition.props.type === 'carriere-longue-non') {
            return "Carrière longue : Non";
        }
        return "!! "+JSON.stringify(condition.props).replace(/\"/g, "'")+" !!";
    };
    
    $scope.delaiToHumanStr = function(delai) {
        if (delai.type === 'AUCUN') {
            return "Aucun";
        }
        if (delai.type === 'DESQUE') {
            return "Le plus tôt possible";
        }
        if (delai.type === 'AUPLUS') {
            return "Au plus tard "+delai.min+" "+(delai.unite === 'MOIS' ? "mois" : (delai.min === 1 ? "année" : "années"));
        }
        if (delai.type === 'ENTRE') {
            return "Entre "+delai.min+" et "+delai.max+" "+(delai.unite === 'MOIS' ? "mois" : "années");
        }
        if (delai.type === 'APARTIR') {
            return "A partir de "+delai.min+" "+(delai.unite === 'MOIS' ? "mois" : (delai.min === 1 ? "année" : "années"));
        }
        if (delai.type === 'SIMPLE') {
            return "A "+delai.min+" "+(delai.unite === 'MOIS' ? "mois" : (delai.min === 1 ? "année" : "années"));
        }
        return "Inconnu : "+JSON.stringify(delai);
    };
    
    $scope.isEditable = function(condition) {
        return (condition.props.type === 'delai');
    };
    
    // Gestion des (copiés-)-collés
    
    $scope.textAreaSetup = function($elt) {
        $elt.on('paste', function(e) {
            var $this = $(this);
            var oldText = $this.html();
            setTimeout(function () {
                var newText = $this.html();
                $this.html(PastedTextCleaner.clean(oldText, newText));
            }, 500);
        });
    };

    // Gestion de l'enregistrement et de la sortie de l'écran
    
    var msgModifiedData = "ATTENTION : les données ont été modifiées. Êtes-vous sûr de vouloir quitter la page ? Les données modifiées seront perdues ! ...";
    
    // . Audition sur les navigations "générales" (navigateur)
    $window.onbeforeunload = function(e) {
        if ($scope.modified) {
            var e = e || window.event;
            // For IE and Firefox
            if (e) {
                e.returnValue = msgModifiedData;
            }
            return msgModifiedData;
        }
    };
    
    // . Audition sur les navigations "internes" ($stateProvider)
    $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        if ($scope.modified) {
            // Par défaut, on désactive la navigation et on pose la question (asynchrone)
            event.preventDefault();
            PromptService.promptQuestion("Confirmation", msgModifiedData).then(function() {
                $scope.modified = false;
                $state.go(toState, toParams);
            });
        }
    });
    
    // Utilitaire
    
    $scope.isSyntaxError = function(text) {
        return SyntaxAnalyzer.isSyntaxError(text);
    }
    
    $scope.countSyntaxErrors = function() {
        var count = 0;
        if ($scope.checklist) {
            angular.forEach($scope.checklist.chapitres, function(chapitre) {
                if (SyntaxAnalyzer.isSyntaxError(chapitre.texteActions)) {
                    count=count+1;
                }
                if (SyntaxAnalyzer.isSyntaxError(chapitre.texteModalites)) {
                    count=count+1;
                }
                if (SyntaxAnalyzer.isSyntaxError(chapitre.texteInfos)) {
                    count=count+1;
                }
            });
        }
        return count;
    }
    
    // Données

    var fnRemoveWatch;
    
    function addWatch() {
        $scope.modified = false;
        fnRemoveWatch = $scope.$watch('checklist', function(newValue, oldValue) {
            if (!ObjectUtils.equalsIgnoringProperties(newValue, oldValue, ['closedInEdition'])) {
                $scope.modified = true;
            }
        }, true);
    }
    
    function storeChecklist(checklist) {
        if (fnRemoveWatch) {
            fnRemoveWatch();
        }
        $scope.checklist = checklist;
        addWatch();
    }
    
    function getChecklistToEdit() {
        var checklist = CheckList.get($stateParams.clid);
        checklist.$promise.then(function() {
            storeChecklist(checklist);
        });
    }
    getChecklistToEdit();
    
});


'use strict';

angular.module('SgmapRetraiteConfig').factory('HttpInterceptor', function ($q, RetraiteToaster, $injector, httpBuffer) {

    return {

        responseError: function (rejection) {

            var UserService = $injector.get('UserService');
            
            if (!UserService.isLogging()) {
                
                // Gestion erreur 401 (authentification)
                if (rejection.status === 401) {
                    console.log("Erreur 401, on redirige vers 'login'...");
                    var $state = $injector.get('$state');
                    $state.go('login');
                    var deferred = $q.defer();
                    httpBuffer.storeRequest({
                        config: rejection.config,
                        deferred: deferred
                    });
                    return deferred.promise;
                }

                // Popup pour notifier l'erreur
                var body = "<pre>Requête : " + rejection.config.method + " " + rejection.config.url + "\nErreur : " + rejection.data+"</pre>";
                RetraiteToaster.popError("Erreur lors d'un accès au serveur !", body);
            
            }

            return $q.reject(rejection);
        }
    };
 });
'use strict';

angular.module('SgmapRetraiteConfig').controller('LoginCtrl', function ($scope, UserService, $state) {

    $scope.user = {};

    $scope.login = function() {
        UserService.login($scope.user);
    };

    $scope.test = function() {
        $state.go('alvar-test');
    };

});

'use strict';

angular.module('SgmapRetraiteConfig').controller('LogoutCtrl', function ($scope, UserService, $state) {

    $scope.user = {};

    $scope.logout = function() {
        UserService.logout();
    };

});

'use strict';

angular.module('SgmapRetraiteConfig').controller('TestCtrl', function ($scope, ApiRegimes, ApiUserChecklist, $timeout, DepartementsUtils, PromptService) {

    // Privé
    
    function createListeMoisAvecPremier() {
        var mois = ['1er Janvier','1er Février','1er Mars','1er Avril','1er Mai','1er Juin','1er Juillet','1er Août','1er Septembre','1er Octobre','1er Novembre','1er Décembre'];
        var listeMoisAvecPremier = [];
        for(var i = 0; i < mois.length; i++) {
            listeMoisAvecPremier.push({
                value: i+1, 
                label: mois[i]
            });
        }
        return listeMoisAvecPremier;
    }
    
    // Actions
    
    $scope.test = function(published) {
        if ($scope.data.regimes.length == 0) {
            PromptService.promptInformation("Erreur !", "Vous devez sélectionner au moins un régime !");
            return;
        }
        $scope.testUrlForIFrame = ApiUserChecklist.getChecklistUrl($scope.data, published, true);
        $scope.reloadIFrame();
    };

    $scope.reloadIFrame = function() {
        var iframe = $('#iframe-test-result');
        iframe.attr('src', iframe.attr('src'));
    };
    
    // Données
    
    ApiRegimes.getRegimes().then(function(regimes) {
        $scope.regimes = regimes;
    });
    
    $scope.departements = DepartementsUtils.createListDepartementsNumber();

    $scope.listeMoisAvecPremier = createListeMoisAvecPremier();
    $scope.listeAnneesDepart = [];
    for(var i = 0; i < 10; i++) {
        $scope.listeAnneesDepart.push(2015+i);
    }
    
    $scope.multiselectRegimesSettings = {
        displayProp: 'name',
        idProp: 'name',
        externalIdProp: '',
        smartButtonMaxItems: 99,
        scrollable: true,
        scrollableHeight: '400px',
        showCheckAll: false,
        showUncheckAll: false,
        groupByTextProvider: function(type) {
            switch (type) {
                case "BASE_ALIGNE":
                    return "Régimes de base alignés";
                case "BASE_AUTRE":
                    return "Autres régimes de base";
                case "COMPLEMENTAIRE":
                    return "Régimes complémentaire";
            } 
            return "toto";
        }
    };
    
    $scope.multiselectRegimesTexts = {
        buttonDefaultText: "Sélectionner les régimes..."
    };

    $scope.data = {
        nom: "DUPONT",
        dateNaissance: "07/10/1954",
        departement: "01",
        departMois: 1,
        departAnnee: 2020,
        regimeLiquidateur: 'CNAV',
        regimes: []
    };
    
});


'use strict';

angular.module('SgmapRetraiteConfig').service('UserService', function ($http, httpBuffer, RetraiteToaster, $state, $rootScope, HttpContextProvider) {

    var isLogging = false;
    
    this.login = function (user) {
        isLogging = true;
        return $http.post(HttpContextProvider.getHttpContext() + '/login?username='+user.name+'&password='+user.pass)
            .success(function (response) {
                isLogging = false;
                $rootScope.$broadcast('userLogged');
                $state.go('configlist');
                httpBuffer.retryLastRequest();
            })
            .error(function(data, status, headers, config ) {
                isLogging = false;
                RetraiteToaster.popErrorWithTimeout("Erreur d'identification !");
            });
    };

    this.logout = function () {
        $http.get(HttpContextProvider.getHttpContext() + '/logout').success(function() {
            $state.reload();
        });
    };

    this.isLogging = function() {
        return isLogging;
    };
});

'use strict';

angular.module('SgmapRetraiteConfig').service('ArrayUtils', function () {

    this.replace = function (array, elementToReplace, newElement) {
        var index = array.indexOf(elementToReplace);
        array.splice(index, 1, newElement);
    };

    this.remove = function (array, elementToRemove) {
        var index = array.indexOf(elementToRemove);
        if (index >= 0) {
            array.splice(index, 1);
        }
    }

});
'use strict';

angular.module('SgmapRetraiteConfig').service('CaissesUtils', function (DepartementsUtils, ArrayUtils) {

    this.searchAvailableDepartements = function (caissesDepartementales) {
        var departements = DepartementsUtils.createListDepartementsNumber();
        angular.forEach(caissesDepartementales, function(caisseDepartementale) {
            angular.forEach(caisseDepartementale.departements, function(departement) {
                ArrayUtils.remove(departements, departement);
            });
        });
        return departements;
    };

});
'use strict';

angular.module('SgmapRetraiteConfig').service('ChecklistPublisher', function ($http, $q, HttpContextProvider) {

    this.publish = function(id) {
        var deferred = $q.defer();
        $http.get(HttpContextProvider.getHttpContext() + '/api/checklistpublish?id='+id).then(function() {
            deferred.resolve();
        });
        return deferred.promise;
    };
});

'use strict';

angular.module('SgmapRetraiteConfig').service('DepartementsUtils', function () {

    this.createListDepartementsNumber = function() {
        var departements = [];
        for(var i = 1; i <= 95; i++) {
            if (i == 20) {
                departements.push("2A");
                departements.push("2B");
            } else {
                departements.push((i < 10 ? "0" : "")+i);
            }
        }
        departements.push("971");
        departements.push("972");
        departements.push("973");
        departements.push("974");
        departements.push("976");
        return departements;
    }
    

});
'use strict';

angular.module('SgmapRetraiteConfig').service('EditConditionValidator', function ($http) {

    this.isDelaiValide = function(condition) {
        if (condition.props.plusOuMoins && condition.props.nombre && condition.props.unite) {
            return;
        }
        return "Vous devez saisir toutes les valeurs";
    };
});
'use strict';

angular.module('SgmapRetraiteConfig').factory('httpBuffer', ['$injector', function ($injector) {
        
    var $http;
    var buffer;

    return {
        storeRequest: function (request) {
            buffer = request;
        },

        retryLastRequest: function () {

            if (!buffer) {
                return;
            }

            function successCallback(response) {
                buffer.deferred.resolve(response);
            }

            function errorCallback(response) {
                buffer.deferred.reject(response);
            }

            $http = $http || $injector.get("$http");
            $http(buffer.config).then(successCallback, errorCallback);
        }
    };
}]);


'use strict';

angular.module('SgmapRetraiteConfig').service('HttpContextProvider', function (Wrapper, $rootScope, $http) {

    this.getHttpContext = function() {
    	
    	return $rootScope.NG_URL_PASSRETRAITE_WS?$rootScope.NG_URL_PASSRETRAITE_WS:'';
    	
    };
    
});
'use strict';

angular.module('SgmapRetraiteConfig').service('ObjectUtils', function () {

    this.equalsIgnoringProperties = function (obj1, obj2, propsToIgnore) {
        obj1 = angular.copy(obj1);
        obj2 = angular.copy(obj2);
        if (propsToIgnore) {
            deletePropertiesInObject(obj1, propsToIgnore);
            deletePropertiesInObject(obj2, propsToIgnore);
        }
        return angular.equals(obj1, obj2);
    };
    
    function deletePropertiesInObject(obj, propsToIgnore) {
        propsToIgnore.forEach(function(propToIgnore) {
            delete obj[propToIgnore];  
        });
        for(var prop in obj) {
            if (startsWith(prop, '$')) {
                continue;
            }
            if (obj.hasOwnProperty(prop)) {
                var subobj = obj[prop];
                if (angular.isArray(subobj)) {
                    angular.forEach(subobj, function(item) {
                        deletePropertiesInObject(item, propsToIgnore);  
                    });
                } else if (subobj instanceof Object) {
                    deletePropertiesInObject(subobj, propsToIgnore); 
                }
            }
        }
    }

});
'use strict';

angular.module('SgmapRetraiteConfig').service('PastedTextCleaner', function () {

    this.clean = function (oldText, newText) {
        var start = 0;
        while (start < oldText.length && oldText[start] == newText[start]) {
            start++;
        }
        if (isInsideTag(oldText, start)) {
            start = previousStartOfTag(oldText, start);
        }
        var endInOld = start;
        var endInNew = newText.length - (oldText.length - start);
        var diffText = newText.substr(start, endInNew - start);
        return oldText.substr(0,start) + cleanHtmlFormat(diffText) + oldText.substr(endInOld);
    };
    
    function cleanHtmlFormat(text) {
        return  text ? String(text).replace(/<[^>]+>/gm, '').replace(/&nbsp;/gm, ' ') : '';
    }
    
    function isInsideTag(text, position) {
        while (position < text.length) {
            if (text[position] === '<') {
                return false;
            }
            if (text[position] === '>') {
                return true;
            }
            position++;
        }
        return false;
    }
    
    function previousStartOfTag(text, position) {
        while (position >= 0 && text[position] !== '<') {
            position--;
        }
        return position < 0 ? position : position;
    }

    function newEndOfTag(text, position) {
        while (position < text.length && text[position] !== '>') {
            position++;
        }
        return position;
    }

});

'use strict';

angular.module('SgmapRetraiteConfig').service('PromptService', function (prompt) {

    this.promptInformation = function (title, message) {
        return prompt({
            title: title,
            message: message,
            buttons: [{ label:'OK', primary: true }]
        });
    };
    
    this.promptQuestion = function (title, message) {
        return prompt({
            title: title,
            message: message
        });
    };
    
});


'use strict';

/*

Ce dialog générique est la base pour tous les dialogs de l'application

A noter qu'il prend en charge le clonage de la valeur à éditer pour ne pas modifier l'original

*/
angular.module('SgmapRetraiteConfig').service('RetraiteDialog', function ($q, ngDialog, TemplateLoader) {

    this.display = function (options) {

        var deferred = $q.defer();

        $q.all({
            templateHtmlContent: TemplateLoader.loadTemplateUrl(options.templateUrl),
            templateHtmlDialog: TemplateLoader.loadTemplateUrl('src/config/utils/retraite-dialog/retraite-dialog.html')
        }).then(function onSuccess(result) {
            
            var templateHtml = result.templateHtmlDialog;
            templateHtml = templateHtml.replace("{{title}}", options.title);
            templateHtml = templateHtml.replace("{{content}}", result.templateHtmlContent);
            
            var dialog = ngDialog.open({
                plain: true,
                template: templateHtml,
                controller: function ($scope) {
                    $scope.value = angular.copy(options.value);
                    $scope.data = angular.copy(options.data);
                    $scope.validateDialog = function () {
                        var error;
                        if (options.canValidate) {
                            error = options.canValidate($scope.value);
                        }
                        if (error) {
                            $scope.error = error;
                        } else {
                            dialog.close();
                            deferred.resolve($scope.value);
                        }
                    };
                    $scope.cancelDialog = function () {
                        dialog.close();
                        deferred.reject();
                    };
                },
                showClose: false
            });

        }, function onError(error) {
            deferred.reject(error);
        });

        return deferred.promise;
    };

});
'use strict';

angular.module('SgmapRetraiteConfig').service('RetraiteToaster', function (toaster) {
    this.popSuccess = function(message) {
        toaster.pop({
            type: "success",
            title: message,
            showCloseButton: true,
            toasterId: "success"
        });
    };

    this.popError = function(message, body) {
        toaster.pop({
            type: "error",
            title: message,
            body: body ? body : "",
            showCloseButton: true,
            toasterId: "error"
        });
    };

    this.popErrorWithTimeout = function(message, body) {
        toaster.pop({
            type: "error",
            title: message,
            body: body ? body : "",
            showCloseButton: true,
            toasterId: "errorTimedout"
        });
    };
});


'use strict';

angular.module('SgmapRetraiteConfig').service('SyntaxAnalyzer', function () {

    this.isSyntaxError = function (text) {
        if (!text) return false;
        return isMustacheVarsSyntaxError(text) || isBracketsLinksSyntaxError(text);
    };

    function isMustacheVarsSyntaxError(text) {
        return isSyntaxError(text, '{{', '}}');
    }
    
    function isBracketsLinksSyntaxError(text) {
        return isSyntaxError(text, '[[', ']]');
    }
    
    function isSyntaxError(text, beginDelimiter, endDelimiter) {
        var begin = text.indexOf(beginDelimiter);
        var end = text.indexOf(endDelimiter);
        if (begin == -1 && end == -1) { 
            return false;
        }
        if (end == -1) {
            return true; 
        }
        if (begin < end) {
            var beginNext = text.indexOf(beginDelimiter, begin+2);
            if (beginNext != -1 && beginNext < end) {
                return true;
            }
            var next = text.substr(end+1);
            return isSyntaxError(next, beginDelimiter, endDelimiter);
        }
        return false;
    }
    
});
'use strict';

angular.module('SgmapRetraiteConfig').service('TemplateLoader', function ($http) {

    this.loadTemplateUrl = function(templateUrl) {
        return $http.get(templateUrl).then(function (res) {
            return res.data || '';
        });
    };

});
'use strict';

angular.module('SgmapRetraiteConfig').service('Wrapper', function () {

    this.getLocation = function () {
        return location;
    };
    
});

