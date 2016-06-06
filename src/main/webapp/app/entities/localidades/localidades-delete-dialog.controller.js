(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('LocalidadesDeleteController',LocalidadesDeleteController);

    LocalidadesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Localidades'];

    function LocalidadesDeleteController($uibModalInstance, entity, Localidades) {
        var vm = this;

        vm.localidades = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Localidades.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
