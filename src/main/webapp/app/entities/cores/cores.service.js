(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Cores', Cores);

    Cores.$inject = ['$resource'];

    function Cores ($resource) {
        var resourceUrl =  'api/cores/:id';

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
