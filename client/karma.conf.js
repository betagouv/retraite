'use strict';

// Karma configuration
module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '',

        // Frameworks to use
        frameworks: ['jasmine'],

        // 'ngResource', 'ngCookies', 'ngAnimate', 'ngTouch', 'ngSanitize', 'ui.router', 'ui.bootstrap', 'ui.utils'
        preprocessors: {
            // source files, that you wanna generate coverage for
            // do not include tests or libraries
            // (these files will be instrumented by Istanbul)
            'app/src/!(tests_fonctionnels)/**/!(*spec|*mock).js': ['coverage']
        },
        
        // List of files / patterns to load in the browser
        files: [
            <!-- injector:bowerjs -->
            'app/lib/jquery/dist/jquery.js',
            'app/lib/bootstrap/dist/js/bootstrap.js',
            'app/lib/angular/angular.js',
            'app/lib/angular-resource/angular-resource.js',
            'app/lib/angular-cookies/angular-cookies.js',
            'app/lib/angular-animate/angular-animate.js',
            'app/lib/angular-touch/angular-touch.js',
            'app/lib/angular-sanitize/angular-sanitize.js',
            'app/lib/angular-bootstrap/ui-bootstrap-tpls.js',
            'app/lib/angular-ui-utils/ui-utils.js',
            'app/lib/angular-ui-router/release/angular-ui-router.js',
            'app/lib/angularjs-toaster/toaster.js',
            'app/lib/bootstrap-switch/dist/js/bootstrap-switch.js',
            'app/lib/ngDialog/js/ngDialog.js',
            'app/lib/angular-prompt/dist/angular-prompt.js',
            'app/lib/rangy/rangy-core.js',
            'app/lib/rangy/rangy-classapplier.js',
            'app/lib/rangy/rangy-highlighter.js',
            'app/lib/rangy/rangy-selectionsaverestore.js',
            'app/lib/rangy/rangy-serializer.js',
            'app/lib/rangy/rangy-textrange.js',
            'app/lib/textAngular/dist/textAngular.js',
            'app/lib/textAngular/dist/textAngular-sanitize.js',
            'app/lib/textAngular/dist/textAngularSetup.js',
            'app/lib/jquery-colorbox/jquery.colorbox.js',
            'app/lib/localforage/dist/localforage.js',
            'app/lib/angular-localforage/dist/angular-localForage.js',
            'app/lib/spin.js/spin.js',
            'app/lib/angular-loading/angular-loading.js',
            'app/lib/angular-mocks/angular-mocks.js',
            'app/lib/angular-xeditable/dist/js/xeditable.js',
            'app/lib/lodash/dist/lodash.compat.js',
            'app/lib/angularjs-dropdown-multiselect/src/angularjs-dropdown-multiselect.js',
            <!-- endinjector -->

            <!-- injector:js -->
            'app/src/config/app-config.js',
            'app/src/config/app-config.module.config.js',
            'app/src/config/app-config.module.run.js',
            'app/src/config/app-id/app-id-controller.js',
            'app/src/config/app-id/app-id-controller.spec.js',
            'app/src/config/caisses/caisses-controller.js',
            'app/src/config/caisses/caisses-controller.spec.js',
            'app/src/config/configlist/configlist-controller.js',
            'app/src/config/configlist/configlist-controller.spec.js',
            'app/src/config/data/api/api-caisse/api-caisse.js',
            'app/src/config/data/api/api-caisse/api-caisse.spec.js',
            'app/src/config/data/api/api-caisses-filter/api-caisses-filter.js',
            'app/src/config/data/api/api-caisses-filter/api-caisses-filter.spec.js',
            'app/src/config/data/api/api-regimes/api-regimes.js',
            'app/src/config/data/api/api-regimes/api-regimes.spec.js',
            'app/src/config/data/api/api-user-checklist/api-user-checklist.js',
            'app/src/config/data/api/api-user-checklist/api-user-checklist.spec.js',
            'app/src/config/data/api/checklist/checklist.js',
            'app/src/config/data/api/checklist/checklist.spec.js',
            'app/src/config/data/ws/ws-caisse-departement/ws-caisse-departement.js',
            'app/src/config/data/ws/ws-caisse-departement/ws-caisse-departement.spec.js',
            'app/src/config/directives/iframe-auto-size.js',
            'app/src/config/edit-caisse/edit-caisse.controller.js',
            'app/src/config/edit-caisse/edit-caisse.controller.spec.js',
            'app/src/config/edit/dialogs/dialog-delai/dialog-delai.js',
            'app/src/config/edit/dialogs/dialog-delai/dialog-delai.spec.js',
            'app/src/config/edit/dialogs/dialog-link/dialog-link.js',
            'app/src/config/edit/dialogs/dialog-link/dialog-link.spec.js',
            'app/src/config/edit/dialogs/dialog-variables/dialog-variables.js',
            'app/src/config/edit/dialogs/dialog-variables/dialog-variables.spec.js',
            'app/src/config/edit/edit-controller.js',
            'app/src/config/edit/edit-controller.spec.js',
            'app/src/config/http-interceptor/http-interceptor.js',
            'app/src/config/http-interceptor/http-interceptor.spec.js',
            'app/src/config/login/login-controller.js',
            'app/src/config/login/login-controller.spec.js',
            'app/src/config/logout/logout-controller.js',
            'app/src/config/logout/logout-controller.spec.js',
            'app/src/config/test/test-controller.js',
            'app/src/config/test/test-controller.spec.js',
            'app/src/config/user-service/user-service.js',
            'app/src/config/user-service/user-service.spec.js',
            'app/src/config/utils/array-utils/array-utils.js',
            'app/src/config/utils/array-utils/array-utils.spec.js',
            'app/src/config/utils/caisses-utils/caisses-utils.js',
            'app/src/config/utils/caisses-utils/caisses-utils.spec.js',
            'app/src/config/utils/checklist-publisher/checklist-publisher.js',
            'app/src/config/utils/checklist-publisher/checklist-publisher.spec.js',
            'app/src/config/utils/departements-utils/departements-utils.js',
            'app/src/config/utils/departements-utils/departements-utils.spec.js',
            'app/src/config/utils/edit-condition-validator/edit-condition-validator.js',
            'app/src/config/utils/edit-condition-validator/edit-condition-validator.spec.js',
            'app/src/config/utils/http-buffer/http-buffer.js',
            'app/src/config/utils/http-buffer/http-buffer.spec.js',
            'app/src/config/utils/http-context-provider/http-context-provider.js',
            'app/src/config/utils/http-context-provider/http-context-provider.spec.js',
            'app/src/config/utils/object-utils/object-utils.js',
            'app/src/config/utils/object-utils/object-utils.spec.js',
            'app/src/config/utils/pasted-text-cleaner/pasted-text-cleaner.js',
            'app/src/config/utils/pasted-text-cleaner/pasted-text-cleaner.spec.js',
            'app/src/config/utils/prompt-service/prompt-service.js',
            'app/src/config/utils/prompt-service/prompt-service.spec.js',
            'app/src/config/utils/retraite-dialog/retraite-dialog.js',
            'app/src/config/utils/retraite-dialog/retraite-dialog.spec.js',
            'app/src/config/utils/retraite-toaster/retraite-toaster.js',
            'app/src/config/utils/retraite-toaster/retraite-toaster.spec.js',
            'app/src/config/utils/syntax-analyzer/syntax-analyzer.js',
            'app/src/config/utils/syntax-analyzer/syntax-analyzer.spec.js',
            'app/src/config/utils/template-loader/template-loader.js',
            'app/src/config/utils/template-loader/template-loader.spec.js',
            'app/src/config/utils/wrapper.js',
            'app/src/constants.js',
            <!-- endinjector -->
            
            // fixtures
            //{pattern: 'tests/mocks/*.json', watched: true, served: true, included: false}
        ],

        // Test results reporter to use
        // Possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
        //reporters: ['progress'],
        reporters: ['progress', 'coverage', 'junit'],

        junitReporter: {
            outputDir: 'report/junit',
            outputFile: 'test-results.xml'
        },
        
        coverageReporter: {
            dir: 'report/coverage'
        },

        // Web server port
        port: 9876,

        // Enable / disable colors in the output (reporters and logs)
        colors: true,

        // Level of logging
        // Possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,

        // Enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'/*,'Chrome','Safari'*/],

        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 60000,

        // Continuous Integration mode
        // If true, it capture browsers, run tests and exit
        singleRun: false
    });
};