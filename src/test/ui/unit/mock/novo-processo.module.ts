namespace app.novoProcesso {
	'use strict';
	
    /** @ngInject * */
    function config() {

        
    }

    angular
        .module('app.novo-processo', ['ui.router', 'pascalprecht.translate'])
        .config(config);
}

namespace app.constants {
	'use strict';

	let url = 'docker';
	let port = "8765";

	angular.module('app.constants', []).constant("properties", {
	    "url": url,
	    "port": port,
	    "apiUrl": url + ":" + port,
	    "development": true
	});
}

namespace app.documentos {
	'use strict';
	
    /** @ngInject * */
    function config() {

        
    }

    angular
        .module('app.documentos', [])
        .config(config);
}