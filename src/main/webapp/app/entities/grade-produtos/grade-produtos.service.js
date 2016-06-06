(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('GradeProdutos', GradeProdutos);

    GradeProdutos.$inject = ['$resource'];

    function GradeProdutos ($resource) {
        var resourceUrl =  'api/grade-produtos/:id';

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
