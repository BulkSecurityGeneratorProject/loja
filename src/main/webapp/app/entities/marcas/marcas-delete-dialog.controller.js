(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('MarcasDeleteController',MarcasDeleteController);

    MarcasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Marcas'];

    function MarcasDeleteController($uibModalInstance, entity, Marcas) {
        var vm = this;

        vm.marcas = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Marcas.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
