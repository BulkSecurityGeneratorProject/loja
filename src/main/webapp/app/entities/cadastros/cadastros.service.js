(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Cadastros', Cadastros);

    Cadastros.$inject = ['$resource'];

    function Cadastros ($resource) {
        var resourceUrl =  'api/cadastros/:id';

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
