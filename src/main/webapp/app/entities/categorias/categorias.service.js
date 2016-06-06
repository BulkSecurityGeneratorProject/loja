(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Categorias', Categorias);

    Categorias.$inject = ['$resource'];

    function Categorias ($resource) {
        var resourceUrl =  'api/categorias/:id';

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
