(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('ProdutosDialogController', ProdutosDialogController);

    ProdutosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Produtos', 'GradeProdutos', 'Itens', 'Marcas', 'Categorias', 'Cores', 'Tamanhos'];

    function ProdutosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Produtos, GradeProdutos, Itens, Marcas, Categorias, Cores, Tamanhos) {
        var vm = this;

        vm.produtos = entity;
        vm.clear = clear;
        vm.save = save;
        vm.gradeprodutos = GradeProdutos.query();
        vm.itens = Itens.query();
        vm.marcas = Marcas.query();
        vm.categorias = Categorias.query();
        vm.cores = Cores.query();
        vm.tamanhos = Tamanhos.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.produtos.id !== null) {
                Produtos.update(vm.produtos, onSaveSuccess, onSaveError);
            } else {
                Produtos.save(vm.produtos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:produtosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
