(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CoresDialogController', CoresDialogController);

    CoresDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cores', 'GradeProdutos', 'Produtos'];

    function CoresDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cores, GradeProdutos, Produtos) {
        var vm = this;

        vm.cores = entity;
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
            if (vm.cores.id !== null) {
                Cores.update(vm.cores, onSaveSuccess, onSaveError);
            } else {
                Cores.save(vm.cores, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:coresUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
