(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('LocalidadesDialogController', LocalidadesDialogController);

    LocalidadesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Localidades'];

    function LocalidadesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Localidades) {
        var vm = this;

        vm.localidades = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.localidades.id !== null) {
                Localidades.update(vm.localidades, onSaveSuccess, onSaveError);
            } else {
                Localidades.save(vm.localidades, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lojaApp:localidadesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
