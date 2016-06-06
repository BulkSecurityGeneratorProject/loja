'use strict';

describe('Controller Tests', function() {

    describe('Categorias Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCategorias, MockGradeProdutos, MockProdutos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCategorias = jasmine.createSpy('MockCategorias');
            MockGradeProdutos = jasmine.createSpy('MockGradeProdutos');
            MockProdutos = jasmine.createSpy('MockProdutos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Categorias': MockCategorias,
                'GradeProdutos': MockGradeProdutos,
                'Produtos': MockProdutos
            };
            createController = function() {
                $injector.get('$controller')("CategoriasDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:categoriasUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
