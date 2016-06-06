(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('ItensDialogController', ItensDialogController);

    ItensDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Itens', 'Pedidos', 'Produtos'];

    function ItensDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Itens, Pedidos, Produtos) {
        var vm = this;

        vm.itens = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pedidos = Pedidos.query();
        vm.produtos = Produtos.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.itens.id !== null) {
                Itens.update(vm.itens, onSaveSuccess, onSaveError);
            } else {
                Itens.save(vm.itens, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:itensUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
