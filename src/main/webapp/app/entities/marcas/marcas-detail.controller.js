(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('MarcasDetailController', MarcasDetailController);

    MarcasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Marcas', 'GradeProdutos', 'Produtos'];

    function MarcasDetailController($scope, $rootScope, $stateParams, entity, Marcas, GradeProdutos, Produtos) {
        var vm = this;

        vm.marcas = entity;

        var unsubscribe = $rootScope.$on('lojaApp:marcasUpdate', function(event, result) {
            vm.marcas = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
