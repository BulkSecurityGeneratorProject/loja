(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CategoriasDialogController', CategoriasDialogController);

    CategoriasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Categorias', 'GradeProdutos', 'Produtos'];

    function CategoriasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Categorias, GradeProdutos, Produtos) {
        var vm = this;

        vm.categorias = entity;
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
            if (vm.categorias.id !== null) {
                Categorias.update(vm.categorias, onSaveSuccess, onSaveError);
            } else {
                Categorias.save(vm.categorias, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:categoriasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
