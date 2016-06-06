(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CategoriasDetailController', CategoriasDetailController);

    CategoriasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Categorias', 'GradeProdutos', 'Produtos'];

    function CategoriasDetailController($scope, $rootScope, $stateParams, entity, Categorias, GradeProdutos, Produtos) {
        var vm = this;

        vm.categorias = entity;

        var unsubscribe = $rootScope.$on('lojaApp:categoriasUpdate', function(event, result) {
            vm.categorias = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
