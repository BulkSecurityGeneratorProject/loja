(function() {
    'use strict';

    angular
        .module('lojaApp')
        .controller('LocalidadesDetailController', LocalidadesDetailController);

    LocalidadesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Localidades'];

    function LocalidadesDetailController($scope, $rootScope, $stateParams, entity, Localidades) {
        var vm = this;

        vm.localidades = entity;

        var unsubscribe = $rootScope.$on('lojaApp:localidadesUpdate', function(event, result) {
            vm.localidades = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
