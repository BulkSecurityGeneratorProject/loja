(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('PedidosDeleteController',PedidosDeleteController);

    PedidosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pedidos'];

    function PedidosDeleteController($uibModalInstance, entity, Pedidos) {
        var vm = this;

        vm.pedidos = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pedidos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
