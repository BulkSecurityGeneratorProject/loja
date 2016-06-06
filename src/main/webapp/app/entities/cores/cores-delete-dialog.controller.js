(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CoresDeleteController',CoresDeleteController);

    CoresDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cores'];

    function CoresDeleteController($uibModalInstance, entity, Cores) {
        var vm = this;

        vm.cores = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cores.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
