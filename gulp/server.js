'use strict';

var path = require('path');
var conf = require('./conf');

var gulp = require('gulp');
var changed = require('gulp-changed');

var through = require('through2');

var browserSync = require('browser-sync');
var browserSyncSpa = require('browser-sync-spa');

var argv = require('yargs').argv;

var watchedFiles = [path.join(conf.paths.bin, '**/*.js')];

browserSync.use(browserSyncSpa({
    selector: '[ng-app]'// Only needed for angular apps
}));

var proxyTarget = "https://docker:8443";
if (argv['proxy-ui']) {
	proxyTarget = "https://localhost:3000";
}

gulp.task('serve:dev', ['watch-ui:bin'], function() {
	// spin up browser sync
	browserSync.init({
		proxy: {
			target: proxyTarget,
			ws: true,
			https: true
		}
	});
});

gulp.task('watch-ui:bin', ['watch'], function() {
	gulp.watch(watchedFiles, function() {
		gulp.src(watchedFiles)
			.pipe(changed(conf.paths.tmp, {hasChanged: changed.compareSha1Digest})) // Calcula o sha1 dos arquivos, para evitar detectar a segunda cópia do arquivo pelo eclipse
			.pipe(through.obj(function(chunk, enc, cb) { // Recarrega e notifica o browsersync apenas se houver mudanças.
				console.log('Mudanças detectadas');
				browserSync.notify('Mudanças detectadas');
				cb(null, chunk);
			}))
			.pipe(gulp.dest(conf.paths.tmp))
			.pipe(browserSync.reload({stream: true, once: true}))
	});
});