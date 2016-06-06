'use strict';

describe('Controller Tests', function() {

    describe('Itens Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItens, MockPedidos, MockProdutos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItens = jasmine.createSpy('MockItens');
            MockPedidos = jasmine.createSpy('MockPedidos');
            MockProdutos = jasmine.createSpy('MockProdutos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Itens': MockItens,
                'Pedidos': MockPedidos,
                'Produtos': MockProdutos
            };
            createController = function() {
                $injector.get('$controller')("ItensDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:itensUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
