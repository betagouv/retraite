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

