(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('PedidosDetailController', PedidosDetailController);

    PedidosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pedidos', 'Itens', 'Cadastros'];

    function PedidosDetailController($scope, $rootScope, $stateParams, entity, Pedidos, Itens, Cadastros) {
        var vm = this;

        vm.pedidos = entity;

        var unsubscribe = $rootScope.$on('lojaApp:pedidosUpdate', function(event, result) {
            vm.pedidos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
