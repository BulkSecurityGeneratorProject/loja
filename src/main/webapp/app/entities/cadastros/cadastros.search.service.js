(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('CadastrosSearch', CadastrosSearch);

    CadastrosSearch.$inject = ['$resource'];

    function CadastrosSearch($resource) {
        var resourceUrl =  'api/_search/cadastros/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
