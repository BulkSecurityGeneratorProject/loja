'use strict';

describe('Controller Tests', function() {

    describe('Tamanhos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTamanhos, MockGradeProdutos, MockProdutos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTamanhos = jasmine.createSpy('MockTamanhos');
            MockGradeProdutos = jasmine.createSpy('MockGradeProdutos');
            MockProdutos = jasmine.createSpy('MockProdutos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Tamanhos': MockTamanhos,
                'GradeProdutos': MockGradeProdutos,
                'Produtos': MockProdutos
            };
            createController = function() {
                $injector.get('$controller')("TamanhosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:tamanhosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
