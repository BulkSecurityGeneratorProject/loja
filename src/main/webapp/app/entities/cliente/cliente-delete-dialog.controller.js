(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('ClienteDeleteController',ClienteDeleteController);

    ClienteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cliente'];

    function ClienteDeleteController($uibModalInstance, entity, Cliente) {
        var vm = this;
        vm.cliente = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Cliente.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
