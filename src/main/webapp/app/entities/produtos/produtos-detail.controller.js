(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('ProdutosDetailController', ProdutosDetailController);

    ProdutosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Produtos', 'GradeProdutos', 'Itens', 'Marcas', 'Categorias', 'Cores', 'Tamanhos'];

    function ProdutosDetailController($scope, $rootScope, $stateParams, entity, Produtos, GradeProdutos, Itens, Marcas, Categorias, Cores, Tamanhos) {
        var vm = this;

        vm.produtos = entity;

        var unsubscribe = $rootScope.$on('lojaApp:produtosUpdate', function(event, result) {
            vm.produtos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
