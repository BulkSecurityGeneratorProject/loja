'use strict';

describe('Controller Tests', function() {

    describe('Produtos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProdutos, MockGradeProdutos, MockItens, MockMarcas, MockCategorias, MockCores, MockTamanhos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProdutos = jasmine.createSpy('MockProdutos');
            MockGradeProdutos = jasmine.createSpy('MockGradeProdutos');
            MockItens = jasmine.createSpy('MockItens');
            MockMarcas = jasmine.createSpy('MockMarcas');
            MockCategorias = jasmine.createSpy('MockCategorias');
            MockCores = jasmine.createSpy('MockCores');
            MockTamanhos = jasmine.createSpy('MockTamanhos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Produtos': MockProdutos,
                'GradeProdutos': MockGradeProdutos,
                'Itens': MockItens,
                'Marcas': MockMarcas,
                'Categorias': MockCategorias,
                'Cores': MockCores,
                'Tamanhos': MockTamanhos
            };
            createController = function() {
                $injector.get('$controller')("ProdutosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:produtosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
