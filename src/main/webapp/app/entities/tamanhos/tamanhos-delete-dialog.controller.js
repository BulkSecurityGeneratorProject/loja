(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('TamanhosDeleteController',TamanhosDeleteController);

    TamanhosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tamanhos'];

    function TamanhosDeleteController($uibModalInstance, entity, Tamanhos) {
        var vm = this;

        vm.tamanhos = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tamanhos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
