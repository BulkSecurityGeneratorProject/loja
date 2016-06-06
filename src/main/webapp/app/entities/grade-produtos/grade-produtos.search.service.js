(function() {
    'use strict';

    angular
        .module('lojaApp')
        .factory('GradeProdutosSearch', GradeProdutosSearch);

    GradeProdutosSearch.$inject = ['$resource'];

    function GradeProdutosSearch($resource) {
        var resourceUrl =  'api/_search/grade-produtos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
