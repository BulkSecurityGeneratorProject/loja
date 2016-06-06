(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Marcas', Marcas);

    Marcas.$inject = ['$resource'];

    function Marcas ($resource) {
        var resourceUrl =  'api/marcas/:id';

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
