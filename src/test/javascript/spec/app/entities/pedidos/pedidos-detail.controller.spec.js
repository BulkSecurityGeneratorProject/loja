'use strict';

describe('Controller Tests', function() {

    describe('Pedidos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPedidos, MockItens, MockCadastros;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPedidos = jasmine.createSpy('MockPedidos');
            MockItens = jasmine.createSpy('MockItens');
            MockCadastros = jasmine.createSpy('MockCadastros');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Pedidos': MockPedidos,
                'Itens': MockItens,
                'Cadastros': MockCadastros
            };
            createController = function() {
                $injector.get('$controller')("PedidosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:pedidosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
