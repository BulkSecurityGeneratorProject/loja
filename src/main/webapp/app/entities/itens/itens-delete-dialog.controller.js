(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('ItensDeleteController',ItensDeleteController);

    ItensDeleteController.$inject = ['$uibModalInstance', 'entity', 'Itens'];

    function ItensDeleteController($uibModalInstance, entity, Itens) {
        var vm = this;

        vm.itens = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Itens.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
