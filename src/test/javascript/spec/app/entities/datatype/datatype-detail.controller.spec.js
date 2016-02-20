'use strict';

describe('Controller Tests', function() {

    describe('Datatype Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDatatype;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDatatype = jasmine.createSpy('MockDatatype');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Datatype': MockDatatype
            };
            createController = function() {
                $injector.get('$controller')("DatatypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'piaApp:datatypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
