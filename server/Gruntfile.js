'use strict';

module.exports = function(grunt) {

    require('load-grunt-tasks')(grunt);

    grunt.initConfig({

        config: {
            app: 'app',
            temp: 'temp',
            sourcesScss: 'sources.scss',
            bowerDir: 'public/libs',
            // Imposé par Play!
            destJs: 'public/javascripts',
            destCss: 'public/stylesheets'
        },

        clean: {
            test: 'report'
        },

        injector: {
            options: {
                addRootSlash: false,
                ignorePath: '<%= config.app %>/',
                bowerPrefix: 'bower',
            },
            bowerDependencies: {
                options: {
                	addRootSlash: true,
                    relative: false
                },
                files: {
                    '<%= config.app %>/views/main.html': ['bower.json'],
                    '<%= config.app %>/views/Application/pdf.html': ['bower.json']
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
                        '<%= config.destJs %>/**/*.js',
                        'testjs/**/*.js'
                    ],
                }
            }
        },

        connect: {
            options: {
                port: 9100,
                hostname: 'localhost',
                livereload: 35729
            },
            /*livereload: {
                options: {
                    open: true,
                    base: [
                        '.tmp',
                        '<%= config.app %>'
                    ]
                }
            },*/
            test: {
                options: {
                    port: 9001,
                    base: [
                        '.tmp',
                        'test',
                        '<%= config.app %>'
                    ]
                }
            }
        },
        
        sass: {
            dist: {
                options: {
                	// paths for @import directives
                    loadPath: [
                        '<%= config.sourcesScss %>'
                    ],
                    style: 'expanded'
                },
                files: {
                    '<%= config.destCss %>/style.css': '<%= config.sourcesScss %>/style.scss'
                }
            }
        },

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
        
        plato: {
            build: {
                files: {
                    'report/plato': [ '<%= config.destJs %>/**/*.js' ]
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
                    alert: true,
                    exports: true,
                    $: true
                }
            },
            all: { 
                options: {},
                src: [
                    './*.js',
                    '!./karma.conf.js',
                    '<%= config.destJs %>/**/*.js'
                ],
            }
        },
        
        watch: {
			sass: {
				files: ['<%= config.sourcesScss %>/**/*.scss'],
				tasks: ['sass'],
				options: {
					spawn: false,
				},
			}
        }

    });

    grunt.registerTask('test', function(target) {
        grunt.task.run([
            'injector',
            target === 'auto' ? 'karma:auto' : 'karma:unit'
        ]);
    });

    grunt.registerTask('build', [
        'sass',
        'injector',
        'karma:unit'
	]);
    
    grunt.registerTask('quality', [
        'sass',
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
