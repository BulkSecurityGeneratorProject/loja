(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('FornecedorDeleteController',FornecedorDeleteController);

    FornecedorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fornecedor'];

    function FornecedorDeleteController($uibModalInstance, entity, Fornecedor) {
        var vm = this;
        vm.fornecedor = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Fornecedor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
