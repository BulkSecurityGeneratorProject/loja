(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Produtos', Produtos);

    Produtos.$inject = ['$resource'];

    function Produtos ($resource) {
        var resourceUrl =  'api/produtos/:id';

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
