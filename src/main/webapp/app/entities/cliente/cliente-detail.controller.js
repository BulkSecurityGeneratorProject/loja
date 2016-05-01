(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('ClienteDetailController', ClienteDetailController);

    ClienteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Cliente'];

    function ClienteDetailController($scope, $rootScope, $stateParams, entity, Cliente) {
        var vm = this;
        vm.cliente = entity;
        
        var unsubscribe = $rootScope.$on('lojaApp:clienteUpdate', function(event, result) {
            vm.cliente = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
