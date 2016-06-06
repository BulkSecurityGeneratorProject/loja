(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('TamanhosSearch', TamanhosSearch);

    TamanhosSearch.$inject = ['$resource'];

    function TamanhosSearch($resource) {
        var resourceUrl =  'api/_search/tamanhos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
