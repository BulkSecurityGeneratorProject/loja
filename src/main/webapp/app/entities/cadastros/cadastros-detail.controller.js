(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CadastrosDetailController', CadastrosDetailController);

    CadastrosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Cadastros', 'Pedidos'];

    function CadastrosDetailController($scope, $rootScope, $stateParams, entity, Cadastros, Pedidos) {
        var vm = this;

        vm.cadastros = entity;

        var unsubscribe = $rootScope.$on('lojaApp:cadastrosUpdate', function(event, result) {
            vm.cadastros = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
