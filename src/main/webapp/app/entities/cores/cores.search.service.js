(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('CoresSearch', CoresSearch);

    CoresSearch.$inject = ['$resource'];

    function CoresSearch($resource) {
        var resourceUrl =  'api/_search/cores/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
