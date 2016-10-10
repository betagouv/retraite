'use strict';

module.exports = function(grunt) {

    require('load-grunt-tasks')(grunt);

    grunt.initConfig({

        config: {
            app: 'app',
            temp: 'temp',
            bowerDir: 'public/libs',
            sourcesScss: 'sources.scss',		// Sources SASS
            scssPages: 'pages',					// Sources SASS pour toutes les pages sauf l'écran final de présentation de la checklist 
            scssChecklist: 'checklist',			// Sources SASS pour l'écran final de présentation de la checklist 
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
                    relative: false,
                    transform: function(filepath) {
                    	if (filepath.endsWith('.css')) {
                    		return '<link rel="stylesheet" href="@{\'' + filepath + '\'}"';
                    	}
                    	else if (filepath.endsWith('.js')) {
                    		return '<script src="@{\'' + filepath + '\'}"></script>';
                    	}
                    }
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
                        'testjs/**/*.js',
                        '<%= config.destJs %>/**/*.js'
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
            pages: {
                options: {
                	// paths for @import directives
                    loadPath: [
                        '<%= config.sourcesScss %>/<%= config.scssPages %>'
                    ],
                    style: 'expanded'
                },
                files: [{
					expand: true,
					cwd: '<%= config.sourcesScss %>/<%= config.scssPages %>',
					src: ['*.scss'],
					dest: '<%= config.destCss %>/<%= config.scssPages %>',
					ext: '.css'
				}]
            },
	        checklist: {
	        	options: {
	        		// paths for @import directives
	        		loadPath: [
	        		           '<%= config.sourcesScss %>/<%= config.scssChecklist %>'
	        		           ],
	        		           style: 'expanded'
	        	},
	        	files: [{
	        		expand: true,
	        		cwd: '<%= config.sourcesScss %>/<%= config.scssChecklist %>',
	        		src: ['*.scss'],
	        		dest: '<%= config.destCss %>/<%= config.scssChecklist %>',
	        		ext: '.css'
	        	}]
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
