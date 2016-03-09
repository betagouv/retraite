'use strict';

// Karma configuration
module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '',

        // Frameworks to use
        frameworks: ['jasmine-jquery','jasmine'],

        // Which plugins to enable
        /*plugins: [
          'karma-jasmine',
          'karma-jasmine-jquery',
          'karma-coverage',
          'karma-phantomjs-launcher',
          'karma-junit-reporter'
        ],*/

        // 'ngResource', 'ngCookies', 'ngAnimate', 'ngTouch', 'ngSanitize', 'ui.router', 'ui.bootstrap', 'ui.utils'
        preprocessors: {
            // source files, that you wanna generate coverage for
            // do not include tests or libraries
            // (these files will be instrumented by Istanbul)
            //'public/javascripts/*.js': ['coverage']
        },
        
        // List of files / patterns to load in the browser
        files: [
            <!-- injector:bowerjs -->
            'public/libs/jquery/dist/jquery.js',
            'public/libs/jquery.maskedinput/dist/jquery.maskedinput.js',
            'public/libs/bootstrap-css/js/bootstrap.min.js',
            'public/libs/jquery-ui/jquery-ui.js',
            <!-- endinjector -->

            <!-- injector:js -->
            'public/javascripts/displayAdditionalQuestions.js',
            'public/javascripts/displayCheckList.js',
            'public/javascripts/displayDepartureDate.js',
            'public/javascripts/displayLiquidateurQuestions.js',
            'public/javascripts/displayQuestionCarriereLongue.js',
            'public/javascripts/getUserData.js',
            'public/javascripts/questions.js',
            'testjs/displayQuestionCarriereLongue.spec.js',
            'testjs/getUserData.spec.js',
            'testjs/questions.spec.js',
            <!-- endinjector -->
            
            // fixtures
            {pattern: 'testjs/spec/javascripts/fixtures/*.html', watched: true, served: true, included: false},
        ],

        // Test results reporter to use
        // Possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
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