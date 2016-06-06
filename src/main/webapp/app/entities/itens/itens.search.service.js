(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('ItensSearch', ItensSearch);

    ItensSearch.$inject = ['$resource'];

    function ItensSearch($resource) {
        var resourceUrl =  'api/_search/itens/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
