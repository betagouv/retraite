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