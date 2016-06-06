(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('ProdutosSearch', ProdutosSearch);

    ProdutosSearch.$inject = ['$resource'];

    function ProdutosSearch($resource) {
        var resourceUrl =  'api/_search/produtos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
