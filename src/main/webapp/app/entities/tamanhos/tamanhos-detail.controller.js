(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('TamanhosDetailController', TamanhosDetailController);

    TamanhosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tamanhos', 'GradeProdutos', 'Produtos'];

    function TamanhosDetailController($scope, $rootScope, $stateParams, entity, Tamanhos, GradeProdutos, Produtos) {
        var vm = this;

        vm.tamanhos = entity;

        var unsubscribe = $rootScope.$on('lojaApp:tamanhosUpdate', function(event, result) {
            vm.tamanhos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
