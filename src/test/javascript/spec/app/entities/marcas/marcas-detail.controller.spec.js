'use strict';

describe('Controller Tests', function() {

    describe('Marcas Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMarcas, MockGradeProdutos, MockProdutos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMarcas = jasmine.createSpy('MockMarcas');
            MockGradeProdutos = jasmine.createSpy('MockGradeProdutos');
            MockProdutos = jasmine.createSpy('MockProdutos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Marcas': MockMarcas,
                'GradeProdutos': MockGradeProdutos,
                'Produtos': MockProdutos
            };
            createController = function() {
                $injector.get('$controller')("MarcasDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:marcasUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
