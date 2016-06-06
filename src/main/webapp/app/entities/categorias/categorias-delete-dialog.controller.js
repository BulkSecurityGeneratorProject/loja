(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CategoriasDeleteController',CategoriasDeleteController);

    CategoriasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Categorias'];

    function CategoriasDeleteController($uibModalInstance, entity, Categorias) {
        var vm = this;

        vm.categorias = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Categorias.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
