'use strict';

describe('Controller Tests', function() {

    describe('Cadastros Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCadastros, MockPedidos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCadastros = jasmine.createSpy('MockCadastros');
            MockPedidos = jasmine.createSpy('MockPedidos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Cadastros': MockCadastros,
                'Pedidos': MockPedidos
            };
            createController = function() {
                $injector.get('$controller')("CadastrosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:cadastrosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
