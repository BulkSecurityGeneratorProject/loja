(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('CategoriasSearch', CategoriasSearch);

    CategoriasSearch.$inject = ['$resource'];

    function CategoriasSearch($resource) {
        var resourceUrl =  'api/_search/categorias/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
