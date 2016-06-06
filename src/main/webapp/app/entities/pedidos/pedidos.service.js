(function() {
    'use strict';
    angular
        .module('lojaApp')
        .factory('Pedidos', Pedidos);

    Pedidos.$inject = ['$resource', 'DateUtils'];

    function Pedidos ($resource, DateUtils) {
        var resourceUrl =  'api/pedidos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dataPedido = DateUtils.convertLocalDateFromServer(data.dataPedido);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataPedido = DateUtils.convertLocalDateToServer(data.dataPedido);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataPedido = DateUtils.convertLocalDateToServer(data.dataPedido);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
