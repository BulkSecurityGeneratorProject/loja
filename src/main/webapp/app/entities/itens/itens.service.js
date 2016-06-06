(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Itens', Itens);

    Itens.$inject = ['$resource'];

    function Itens ($resource) {
        var resourceUrl =  'api/itens/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
