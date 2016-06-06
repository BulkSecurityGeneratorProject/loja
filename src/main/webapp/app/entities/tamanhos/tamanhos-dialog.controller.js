(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('TamanhosDialogController', TamanhosDialogController);

    TamanhosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tamanhos', 'GradeProdutos', 'Produtos'];

    function TamanhosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tamanhos, GradeProdutos, Produtos) {
        var vm = this;

        vm.tamanhos = entity;
        vm.clear = clear;
        vm.save = save;
        vm.gradeprodutos = GradeProdutos.query();
        vm.produtos = Produtos.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tamanhos.id !== null) {
                Tamanhos.update(vm.tamanhos, onSaveSuccess, onSaveError);
            } else {
                Tamanhos.save(vm.tamanhos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:tamanhosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
