(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('FornecedorDialogController', FornecedorDialogController);

    FornecedorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fornecedor'];

    function FornecedorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fornecedor) {
        var vm = this;
        vm.fornecedor = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('lojaApp:fornecedorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.fornecedor.id !== null) {
                Fornecedor.update(vm.fornecedor, onSaveSuccess, onSaveError);
            } else {
                Fornecedor.save(vm.fornecedor, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
