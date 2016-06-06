(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('ProdutosDeleteController',ProdutosDeleteController);

    ProdutosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Produtos'];

    function ProdutosDeleteController($uibModalInstance, entity, Produtos) {
        var vm = this;

        vm.produtos = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Produtos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
