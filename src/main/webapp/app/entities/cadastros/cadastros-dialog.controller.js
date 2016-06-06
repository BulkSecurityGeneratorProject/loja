(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CadastrosDialogController', CadastrosDialogController);

    CadastrosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cadastros', 'Pedidos'];

    function CadastrosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cadastros, Pedidos) {
        var vm = this;

        vm.cadastros = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pedidos = Pedidos.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cadastros.id !== null) {
                Cadastros.update(vm.cadastros, onSaveSuccess, onSaveError);
            } else {
                Cadastros.save(vm.cadastros, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:cadastrosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
