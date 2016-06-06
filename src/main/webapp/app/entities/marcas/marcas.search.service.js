(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('MarcasSearch', MarcasSearch);

    MarcasSearch.$inject = ['$resource'];

    function MarcasSearch($resource) {
        var resourceUrl =  'api/_search/marcas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
