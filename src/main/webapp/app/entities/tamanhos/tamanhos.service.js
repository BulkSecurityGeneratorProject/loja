(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Tamanhos', Tamanhos);

    Tamanhos.$inject = ['$resource'];

    function Tamanhos ($resource) {
        var resourceUrl =  'api/tamanhos/:id';

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
