(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('PedidosDialogController', PedidosDialogController);

    PedidosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pedidos', 'Itens', 'Cadastros'];

    function PedidosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pedidos, Itens, Cadastros) {
        var vm = this;

        vm.pedidos = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.itens = Itens.query();
        vm.cadastros = Cadastros.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pedidos.id !== null) {
                Pedidos.update(vm.pedidos, onSaveSuccess, onSaveError);
            } else {
                Pedidos.save(vm.pedidos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:pedidosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dataPedido = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
