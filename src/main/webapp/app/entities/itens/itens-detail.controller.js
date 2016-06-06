(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('ItensDetailController', ItensDetailController);

    ItensDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Itens', 'Pedidos', 'Produtos'];

    function ItensDetailController($scope, $rootScope, $stateParams, entity, Itens, Pedidos, Produtos) {
        var vm = this;

        vm.itens = entity;

        var unsubscribe = $rootScope.$on('lojaApp:itensUpdate', function(event, result) {
            vm.itens = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
