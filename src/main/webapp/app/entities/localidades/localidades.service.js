(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Localidades', Localidades);

    Localidades.$inject = ['$resource'];

    function Localidades ($resource) {
        var resourceUrl =  'api/localidades/:id';

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
