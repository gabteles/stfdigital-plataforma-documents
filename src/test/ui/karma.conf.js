'use strict';

var path = require('path');
var conf = require('./../../../gulp/conf');

var _ = require('lodash');
var wiredep = require('wiredep');

var pathSrcHtml = [
	path.join(conf.paths.src, '/**/*.html')
];

var pathSrcJs = [
	path.resolve(path.join(conf.paths.dist, '/**/!(*.spec).js'))
];

function listIncludeFiles() {
	var wiredepOptions = _.extend({}, conf.wiredep, {
	    dependencies: true,
	    devDependencies: true
	});

	var patterns = wiredep(wiredepOptions).js;
		//.concat(pathSrcJs)
		//.concat(pathSrcHtml);

	console.log(patterns); // TODO Recuperar os caminhos dinamicamente.
	
	 return ['../ui/bower_components/angular/angular.js',
     '../ui/bower_components/angular-mocks/angular-mocks.js',
     '../ui/bower_components/angular-ui-router/release/angular-ui-router.js',
     '../ui/bower_components/angular-translate/angular-translate.js',
     '../ui/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
     path.join(conf.paths.test, 'unit/mock/**/*.js')]
	
//	patterns.push(path.join(conf.paths.test, 'unit/mock/**/*.js'));
//	
//	return patterns;
}

function listFiles() {
  var patterns = listIncludeFiles();
  
  patterns.push('src/main/resources/public/modelos.js');
  
  var files = patterns.map(function(pattern) {
    return {
      pattern: pattern,
      included: false
    };
  });
  files.push(path.join(conf.paths.test, 'unit/app/**/*.js'));
  return files;
}
module.exports = function(config) {
  var configuration = {
    files: listFiles(),

    singleRun: true,
    
    basePath: '../../..',

    autoWatch: false,

    logLevel: 'info',

    frameworks: ['systemjs', 'jasmine'],

    browsers : ['Chrome'],

    plugins : [
      'karma-systemjs',
      'karma-phantomjs-launcher',
      'karma-chrome-launcher',
      //'karma-coverage',
      'karma-jasmine',
	  'karma-html-reporter',
	  'karma-mocha-reporter'
    ],

    coverageReporter: {
      type : 'html',
      dir : 'coverage/'
    },

    reporters: ['mocha', 'html'],

    htmlReporter : {
		outputDir : path.join(conf.paths.unit, 'results/html')
	},
    
    systemjs: {
    	configFile:  path.join(conf.paths.test, 'system.conf.js'),
    	serveFiles: ['src/main/resources/public/modelos.js', 'src/main/resources/public/maps/modelos.js.map'],
    	includeFiles: listIncludeFiles()
    },

    proxies: {
      '/base/documents/modelos': '/base/src/main/resources/public/modelos.js',
      '/base/documents/maps/modelos.js.map': '/base/src/main/resources/public/maps/modelos.js.map'
    }
  };

  // This is the default preprocessors configuration for a usage with Karma cli
  // The coverage preprocessor is added in gulp/unit-test.js only for single tests
  // It was not possible to do it there because karma doesn't let us now if we are
  // running a single test or not
//  configuration.preprocessors = {};
//  pathSrcHtml.forEach(function(path) {
//    configuration.preprocessors[path] = ['ng-html2js'];
//  });
//
  config.set(configuration);
};
