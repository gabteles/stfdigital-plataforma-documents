System.config({
	transpiler: null,
	defaultJSExtensions: true,
	map: {
		'systemjs': './node_modules/systemjs/dist/system.src.js',
		'system-polyfills': './node_modules/systemjs/dist/system-polyfills.src.js'
	},
	bundles: {
		'documents/modelos': ['documents/modelos/*']
	},
	strictImportSequence: true
});
