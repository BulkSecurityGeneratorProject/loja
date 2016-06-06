(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('MarcasDialogController', MarcasDialogController);

    MarcasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Marcas', 'GradeProdutos', 'Produtos'];

    function MarcasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Marcas, GradeProdutos, Produtos) {
        var vm = this;

        vm.marcas = entity;
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
            if (vm.marcas.id !== null) {
                Marcas.update(vm.marcas, onSaveSuccess, onSaveError);
            } else {
                Marcas.save(vm.marcas, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:marcasUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
