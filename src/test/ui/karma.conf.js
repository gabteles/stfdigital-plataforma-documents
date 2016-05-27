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

function listFiles() {
  var wiredepOptions = _.extend({}, conf.wiredep, {
    dependencies: true,
    devDependencies: true
  });

  var patterns = wiredep(wiredepOptions).js
      .concat(pathSrcJs)
      .concat(pathSrcHtml);

  var files = patterns.map(function(pattern) {
    return {
      pattern: pattern
    };
  });
  files.push({
    pattern: path.join(conf.paths.src, '/assets/**/*'),
    included: false,
    served: true,
    watched: false
  });
  return files;
}

module.exports = function(config) {
console.log(listFiles());
  var configuration = {
    files: [{pattern: '../ui/bower_components/angular/angular.js', included: false},
            {pattern: '../ui/bower_components/angular-mocks/angular-mocks.js', included: false},
            {pattern: '../ui/bower_components/angular-ui-router/release/angular-ui-router.js', included: false},
            {pattern: '../ui/bower_components/angular-translate/angular-translate.js', included: false},
            {pattern: '../ui/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js', included: false},
            {pattern: path.join(conf.paths.test, 'unit/mock/**/*.js'), included: false},
            {pattern: 'src/main/resources/public/modelos.js', included: false},
            path.join(conf.paths.test, 'unit/app/**/*.js')],

    singleRun: true,
    
    basePath: '../../..',

    autoWatch: false,

    ngHtml2JsPreprocessor: {
      stripPrefix: conf.paths.src + '/',
      moduleName: 'generatorGulpAngular'
    },

    logLevel: 'WARN',

    frameworks: ['systemjs', 'jasmine'/*, 'angular-filesort'*/],

//    angularFilesort: {
//      whitelist: [path.join(conf.paths.src, '/**/!(*.html|*.spec|*.mock).js')]
//    },

    browsers : ['Chrome'],

    plugins : [
      'karma-systemjs',
      'karma-phantomjs-launcher',
//      'karma-angular-filesort',
      'karma-chrome-launcher',
      //'karma-coverage',
      'karma-jasmine',
      'karma-ng-html2js-preprocessor',
	  'karma-html-reporter',
	  'karma-mocha-reporter' 
    ],

    coverageReporter: {
      type : 'html',
      dir : 'coverage/'
    },

    reporters: ['progress'],
    
    systemjs: {
    	configFile:  path.join(conf.paths.test, 'system.conf.js'),
    	serveFiles: ['src/main/resources/public/modelos.js', 'src/main/resources/public/maps/modelos.js.map'],
    	includeFiles: ['../ui/bower_components/angular/angular.js',
    	               '../ui/bower_components/angular-mocks/angular-mocks.js',
    	               '../ui/bower_components/angular-ui-router/release/angular-ui-router.js',
    	               '../ui/bower_components/angular-translate/angular-translate.js',
    	               '../ui/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
    	               path.join(conf.paths.test, 'unit/mock/**/*.js')]
    },

    proxies: {
      '/assets/': path.join('/base/', conf.paths.src, '/assets/'),
      '/base/documents/modelos': '/base/src/main/resources/public/modelos.js',
      '/base/documents/maps/modelos.js.map': '/base/src/main/resources/public/maps/modelos.js.map'
    }
  };

  // This is the default preprocessors configuration for a usage with Karma cli
  // The coverage preprocessor is added in gulp/unit-test.js only for single tests
  // It was not possible to do it there because karma doesn't let us now if we are
  // running a single test or not
  configuration.preprocessors = {};
  pathSrcHtml.forEach(function(path) {
    configuration.preprocessors[path] = ['ng-html2js'];
  });

  config.set(configuration);
};
