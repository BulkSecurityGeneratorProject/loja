(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('LocalidadesSearch', LocalidadesSearch);

    LocalidadesSearch.$inject = ['$resource'];

    function LocalidadesSearch($resource) {
        var resourceUrl =  'api/_search/localidades/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
