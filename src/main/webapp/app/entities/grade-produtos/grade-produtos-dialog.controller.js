(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('GradeProdutosDialogController', GradeProdutosDialogController);

    GradeProdutosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GradeProdutos', 'Marcas', 'Categorias', 'Cores', 'Tamanhos', 'Produtos'];

    function GradeProdutosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GradeProdutos, Marcas, Categorias, Cores, Tamanhos, Produtos) {
        var vm = this;

        vm.gradeProdutos = entity;
        vm.clear = clear;
        vm.save = save;
        vm.marcas = Marcas.query();
        vm.categorias = Categorias.query();
        vm.cores = Cores.query();
        vm.tamanhos = Tamanhos.query();
        vm.produtos = Produtos.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.gradeProdutos.id !== null) {
                GradeProdutos.update(vm.gradeProdutos, onSaveSuccess, onSaveError);
            } else {
                GradeProdutos.save(vm.gradeProdutos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:gradeProdutosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
