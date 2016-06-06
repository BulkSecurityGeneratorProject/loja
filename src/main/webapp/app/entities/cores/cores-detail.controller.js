(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('CoresDetailController', CoresDetailController);

    CoresDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Cores', 'GradeProdutos', 'Produtos'];

    function CoresDetailController($scope, $rootScope, $stateParams, entity, Cores, GradeProdutos, Produtos) {
        var vm = this;

        vm.cores = entity;

        var unsubscribe = $rootScope.$on('lojaApp:coresUpdate', function(event, result) {
            vm.cores = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
