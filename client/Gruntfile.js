'use strict';

module.exports = function(grunt) {

    require('load-grunt-tasks')(grunt);
    require('time-grunt')(grunt);
    var rewriteRulesSnippet = require('grunt-connect-rewrite/lib/utils').rewriteRequest;

    // Pour faire une pause à chaque task : grunt build --pause
    if (grunt.option('pause')) {
        var pause = require("grunt-pause");
        pause.init(grunt);
    }

    var checklists = [
        {
            nom: "CNAV",
            id: 1
        },{
            nom: "RSI",
            id: 2
        },{
            nom: "MSA",
            id: 3
        }
    ];

    var checklist = {
        nom:"RSI",
        chapitres:[
            {
                titre:"Je prépare ma cessation d\u0027activité",
                id:4
            },{
                titre:"Je reconstiture ma carrière",
                id:5
            },{
                titre:"Je contacte mes autres complémentaires",
                id:6
            }
        ],
        id:2
    };
    
    var regimesAlignes = [{
        name: "CAVIMAC"},{
        name: "CNAV"},{
        name:"MSA"},{
        name:"RSI"
    }];

    var mockApiData = function(req, res, next) {
        var url = req.url;
        if (url === "/apirestregimes/getregimesbasealignes") {
            res.end(JSON.stringify(regimesAlignes));
            return;
        }
        if (url === "/api/checklist") {
            res.end(JSON.stringify(checklists));
            return;
        }
        if (url.indexOf("/api/checklist/") === 0) {
            res.end(JSON.stringify(checklist));
            return;
        }
        next();
    };

    grunt.initConfig({

        config: {
            app: 'app',
            temp: 'temp',
            dist: 'www',
            bowerDir: 'lib'
        },

        watch: {

            bower: {
                files: ['bower.json'],
                tasks: ['injector']
            },

            js: {
                files: [
                    '<%= config.app %>/src/{,*/}*.js'
                ],
                tasks: ['newer:jshint:all'],
                options: {
                    livereload: true
                }
            },

            jsUnitTest: {
                files: ['<%= config.app %>/modules/*/tests/unit/*.js'],
                tasks: ['karma:unit']
            },

            styles: {
                files: ['<%= config.app %>/css/{,*/}*.css'],
                tasks: ['newer:copy:styles', 'autoprefixer']
            },

            gruntfile: {
                files: ['Gruntfile.js']
            },

            livereload: {
                options: {
                    livereload: '<%= connect.options.livereload %>'
                },
                files: [
                    '<%= config.app %>/**/*.html',
                    '.tmp/css/{,*/}*.css',
                    '<%= config.app %>/img/{,*/}*.{png,jpg,jpeg,gif,webp,svg}'
                ]
            }
        },

        connect: {
            options: {
                port: 9100,
                hostname: 'localhost',
                livereload: 35729
            },
            rules: [
                {from: '^/config/lib/(.*)$', to: 'http://localhost:9100/lib/$1', redirect: 'permanent'},
                {from: '^/images/(.*)$', to: 'http://192.168.1.10:9000/images/$1', redirect: 'permanent'},
                {from: '^/img/(.*)$', to: 'http://192.168.1.10:9000/images/$1', redirect: 'permanent'}
            ],
            livereload: {
                options: {
                    open: true,
                    base: [
                        '.tmp',
                        '<%= config.app %>'
                    ],
                    middleware: function (connect, options, middlewares ) {
                        middlewares.unshift(mockApiData);
                        middlewares.unshift(function(req, res, next) {
                            res.setHeader('Access-Control-Allow-Origin ', '*');
                            res.setHeader('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
                            res.setHeader('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS');
                            next();
                        });
                        middlewares.push(rewriteRulesSnippet);
                        return middlewares;
                    }
                }
            },
            test: {
                options: {
                    port: 9001,
                    base: [
                        '.tmp',
                        'test',
                        '<%= config.app %>'
                    ]
                }
            },
            dist: {
                options: {
                    base: ['<%= config.app %>']
                }
            }
        },

        jshint: {
            options: {
                "bitwise": true,
                "immed": true,
                "newcap": true,
                "noarg": true,
                "noempty": true,
                "nonew": true,
                "trailing": true,
                "maxlen": 200,
                "boss": true,
                "eqnull": true,
                "expr": true,
                "globalstrict": true,
                "laxbreak": true,
                "loopfunc": true,
                "sub": true,
                "undef": true,
                "indent": 2,
                curly: true,
                browser: true,
                forin: true,
                freeze: true,
                reporter: require('jshint-html-reporter'),
                reporterOutput: 'report/jshint-report.html',
                globals: {
                    angular: true,
                    module: true,
                    require: true,
                    console: true,
                    exports: true,
                    describe: true,
                    beforeEach: true,
                    afterEach: true,
                    it: true,
                    jasmine: true,
                    expect: true,
                    spyOn: true,
                    inject: true,
                    browser: true,
                    element: true,
                    by: true
                }
            },
            all: { 
                options: {},
                src: [
                    './*.js',
                    '!./karma.conf.js',
                    '<%= config.app %>/src/**/*.js'
                ],
            }
        },

        plato: {
            build: {
                files: {
                    'report/plato': [ '<%= config.app %>/src/**/*.js' ]
                }
            }
        },
        
        clean: {
            dist: {
                options: {
                    force: true
                },
                files: [{
                    dot: true,
                    src: [
                        '.tmp/*',
                        '<%= config.dist %>/*',
                        '!<%= config.dist %>/.git*'
                    ]
                }]
            },
            server: '.tmp/*',
            test: 'report'
        },

        autoprefixer: {
            options: {
                browsers: ['last 1 version']
            },
            dist: {
                files: [{
                    expand: true,
                    cwd: '.tmp/styles/',
                    src: '**/*.css',
                    dest: '.tmp/styles/'
                }]
            }
        },

        /**
        * Strip comments from the distribution code
        */
        comments: {
            dist: {
                options: {
                    singleline: true,
                    multiline: true
                },
                src: [ 'www/scripts/custom.js']
            },
        },

        //Injects all the scripts into the index html file
        injector: {
            options: {
                addRootSlash: false,
                ignorePath: '<%= config.app %>/',
                bowerPrefix: 'bower',
            },
            localDependencies: {
                options: {
                    relative: false
                },
                files: {
                    '<%= config.app %>/index.html': [
                        '<%= config.app %>/src/*.js',
                        '<%= config.app %>/src/**/*.js',
                        '!<%= config.app %>/src/**/*.spec.js',
                        '!<%= config.app %>/src/tests_fonctionnels/**/*.js',
                        '<%= config.app %>/css/{,*/}*.css',
                        '<%= config.app %>/src/**/*.css',
                        '<%= config.app %>/lib/angular-i18n/angular-locale_fr-fr.js'
                    ],
                    '<%= config.app %>/config.html': [
                        '<%= config.app %>/src/*.js',
                        '<%= config.app %>/src/**/*.js',
                        '!<%= config.app %>/src/**/*.spec.js',
                        '!<%= config.app %>/src/tests_fonctionnels/**/*.js',
                        '<%= config.app %>/css/{,*/}*.css',
                        '<%= config.app %>/src/**/*.css',
                        '<%= config.app %>/lib/angular-i18n/angular-locale_fr-fr.js'
                    ]
                }
            },
            bowerDependencies: {
                options: {
                    relative: true
                },
                files: {
                    '<%= config.app %>/index.html': ['bower.json'],
                    '<%= config.app %>/config.html': ['bower.json']
                }
            },
            karmaDependencies: {
                options: {
                    ignorePath: '',
                    transform: function(filepath) {
                        return '\'' + filepath + '\',';
                    }
                },
                files: {
                    'karma.conf.js': [
                        'bower.json',
                        '<%= config.app %>/src/**/*.js',
                        '!<%= config.app %>/src/tests_fonctionnels/**/*.js'
                    ],
                }
            }
        },

        // Reads HTML for usemin blocks to enable smart builds that automatically
        // concat, minify and revision files. Creates configurations in memory so
        // additional tasks can operate on them
        useminPrepare: {
            html: '<%= config.app %>/*.html',
            options: {
                dest: '<%= config.dist %>',
                flow: {
                    html: {
                        steps: {
                            js: ['concat'],
                            css: ['cssmin']
                        },
                        post: {}
                    }
                }
            }
        },

        // Performs rewrites based on rev and the useminPrepare configuration
        usemin: {
            html: ['<%= config.dist %>/**/*.html'],
            css: ['<%= config.dist %>/styles/{,*/}*.css'],
            options: {
                assetsDirs: ['<%= config.dist %>']
            }
        },

        // The following *-min tasks produce minified files in the dist folder
        /*cssmin: {
            options: {
                root: '<%= config.<%= config.app %> %>/css/** /*.css'
            }
        },*/

        imagemin: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '<%= config.app %>/img',
                    src: '{,*/}*.{png,jpg,jpeg,gif}',
                    dest: '<%= config.dist %>/img'
                }]
            }
        },

        svgmin: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '<%= config.app %>/img',
                    src: '{,*/}*.svg',
                    dest: '<%= config.dist %>/img'
                }]
            }
        },

        htmlmin: {
            dist: {
                options: {
                    collapseWhitespace: true,
                    collapseBooleanAttributes: true,
                    removeCommentsFromCDATA: true
                },
                files: [{
                    expand: true,
                    cwd: '<%= config.dist %>',
                    src: ['*.html', '<%= config.app %>/modules/*/views/*.html'],
                    dest: '<%= config.dist %>'
                }]
            }
        },

        // ngmin tries to make the code safe for minification automatically by
        // using the Angular long form for dependency injection. It doesn't work on
        // things like resolve or inject so those have to be done manually.
        ngmin: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '.tmp/concat/<%= config.app %>/js',
                    src: '*.js',
                    dest: '.tmp/concat/<%= config.app %>/js'
                }]
            }
        },

        // Copies remaining files to places other tasks can use
        copy: {
            dist: {
                files: [{
                    expand: true,
                    dot: true,
                    cwd: '<%= config.app %>',
                    dest: '<%= config.dist %>',
                    src: [
                        '*.{ico,png,txt}',
                        '.htaccess',
                        'index.html',
                        'config.html',
                        '**/*.html',
                        'img/{,*/}*.{webp}',
                        'css/images/*',
                        'fonts/*'
                    ]
                },{
                    expand: true,
                    cwd: '.tmp/images',
                    dest: '<%= config.dist %>/img',
                    src: ['generated/*']
                },{
                    expand: true,
                    cwd: '<%= config.app %>/<%= config.bowerDir %>',
                    dest: '<%= config.dist %>/fonts',
                    src: '**/fonts/*.*',
                    flatten: true
                }]
            },
            styles: {
                expand: true,
                cwd: '<%= config.app %>/css',
                dest: '.tmp/css/',
                src: '**/*.css'
            }
        },

        // Test settings
        karma: {
            options: {
                configFile: 'karma.conf.js',
                singleRun: true
            },
            unit: {
                singleRun: true
            },
            auto: {
                singleRun: false
            }
        },
        
    });


    grunt.registerTask('serve', function(target) {
        if (target === 'dist') {
            console.log('dist serve');
            return grunt.task.run(['build', 'connect:dist:keepalive']);
        }

        grunt.task.run([
            'clean:server',
            'injector',
            'autoprefixer',
            'configureRewriteRules',
            'connect:livereload',
            'watch'
        ]);
    });

    grunt.registerTask('server', function(target) {
        grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
        grunt.task.run(['serve:' + target]);
    });

    grunt.registerTask('test', function(target) {
        grunt.task.run([
            'clean:server',
            'injector',
            'autoprefixer',
            'connect:test',
            target === 'auto' ? 'karma:auto' : 'karma:unit'
        ]);
    });

    grunt.registerTask('build', 'Build', function (target) {
        grunt.task.run([
            'clean:dist',
            'injector',
            'useminPrepare',
            'autoprefixer',
            'concat',
            'ngmin',
            'copy:dist',
            'cssmin',
            'usemin',
            'comments:dist'
        ]);
    });
    
    grunt.registerTask('quality', [
        'injector',
        'plato',
        'jshint'
    ]);
    
    grunt.registerTask('default', [
        'clean:test',
        'quality',
        'test',
        'build'
    ]);
};
