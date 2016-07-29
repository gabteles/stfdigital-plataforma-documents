import ITranslatePartialLoaderService = angular.translate.ITranslatePartialLoaderService;

/** @ngInject **/
function run($translatePartialLoader: ITranslatePartialLoaderService, properties: app.support.constants.Properties) {
    $translatePartialLoader.addPart(properties.apiUrl + '/documents/dashlets');
}

let dashlets: ng.IModule = angular.module('app.documents.dashlets', ['app.support.dashboards']);
dashlets.run(run);

export default dashlets;