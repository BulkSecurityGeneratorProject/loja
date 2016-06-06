(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('PedidosSearch', PedidosSearch);

    PedidosSearch.$inject = ['$resource'];

    function PedidosSearch($resource) {
        var resourceUrl =  'api/_search/pedidos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
