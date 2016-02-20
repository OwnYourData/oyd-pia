'use strict';

describe('Controller Tests', function() {

    describe('Data Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockData, MockDatatype;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockData = jasmine.createSpy('MockData');
            MockDatatype = jasmine.createSpy('MockDatatype');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Data': MockData,
                'Datatype': MockDatatype
            };
            createController = function() {
                $injector.get('$controller')("DataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'piaApp:dataUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
