(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('GradeProdutosDeleteController',GradeProdutosDeleteController);

    GradeProdutosDeleteController.$inject = ['$uibModalInstance', 'entity', 'GradeProdutos'];

    function GradeProdutosDeleteController($uibModalInstance, entity, GradeProdutos) {
        var vm = this;

        vm.gradeProdutos = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GradeProdutos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
