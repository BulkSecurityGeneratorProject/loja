(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('FornecedorDetailController', FornecedorDetailController);

    FornecedorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Fornecedor'];

    function FornecedorDetailController($scope, $rootScope, $stateParams, entity, Fornecedor) {
        var vm = this;
        vm.fornecedor = entity;
        
        var unsubscribe = $rootScope.$on('lojaApp:fornecedorUpdate', function(event, result) {
            vm.fornecedor = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
