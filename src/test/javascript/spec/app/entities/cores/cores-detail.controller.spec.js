'use strict';

describe('Controller Tests', function() {

    describe('Cores Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCores, MockGradeProdutos, MockProdutos;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCores = jasmine.createSpy('MockCores');
            MockGradeProdutos = jasmine.createSpy('MockGradeProdutos');
            MockProdutos = jasmine.createSpy('MockProdutos');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Cores': MockCores,
                'GradeProdutos': MockGradeProdutos,
                'Produtos': MockProdutos
            };
            createController = function() {
                $injector.get('$controller')("CoresDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lojaApp:coresUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
