'use strict';

describe('Controller Tests', function() {

    describe('GradeProdutos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGradeProdutos, MockMarcas, MockCategorias, MockCores, MockTamanhos, MockProdutos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGradeProdutos = jasmine.createSpy('MockGradeProdutos');
            MockMarcas = jasmine.createSpy('MockMarcas');
            MockCategorias = jasmine.createSpy('MockCategorias');
            MockCores = jasmine.createSpy('MockCores');
            MockTamanhos = jasmine.createSpy('MockTamanhos');
            MockProdutos = jasmine.createSpy('MockProdutos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'GradeProdutos': MockGradeProdutos,
                'Marcas': MockMarcas,
                'Categorias': MockCategorias,
                'Cores': MockCores,
                'Tamanhos': MockTamanhos,
                'Produtos': MockProdutos
            };
            createController = function() {
                $injector.get('$controller')("GradeProdutosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:gradeProdutosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
