(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CadastrosDeleteController',CadastrosDeleteController);

    CadastrosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cadastros'];

    function CadastrosDeleteController($uibModalInstance, entity, Cadastros) {
        var vm = this;

        vm.cadastros = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cadastros.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
