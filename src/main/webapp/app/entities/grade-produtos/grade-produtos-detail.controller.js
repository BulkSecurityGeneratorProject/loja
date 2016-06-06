(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('GradeProdutosDetailController', GradeProdutosDetailController);

    GradeProdutosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'GradeProdutos', 'Marcas', 'Categorias', 'Cores', 'Tamanhos', 'Produtos'];

    function GradeProdutosDetailController($scope, $rootScope, $stateParams, entity, GradeProdutos, Marcas, Categorias, Cores, Tamanhos, Produtos) {
        var vm = this;

        vm.gradeProdutos = entity;

        var unsubscribe = $rootScope.$on('lojaApp:gradeProdutosUpdate', function(event, result) {
            vm.gradeProdutos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
