'use strict';

describe('displayQuestionCarriereLongue', function () {

	beforeEach(function () {
		jasmine.getFixtures().fixturesPath = 'base/testjs/spec/javascripts/fixtures';
		loadFixtures('displayQuestionCarriereLongue.html');
		DisplayQuestionCarriereLongue.initJquery(); 
	});

    it('should be disabled initialy', function () {
    	expectNextButtonIsDisabled();
    });
    
    it('should be enabled after input checkbox is clicked', function (done) {
    	
    	$('[name=CARRIERE_LONGUE]').trigger( "click" );
		
    	$(document).on('DisplayQuestionCarriereLongue:diplayUpdated', function() {
			// Il faut se désabonner de l'évènement pour éviter le mélange entre chaque TU
			$(document).off('DisplayQuestionCarriereLongue:diplayUpdated');
			expectNextButtonIsEnabled(); 
			done();
		}); 
    });
    
    function expectNextButtonIsDisabled() {
    	expect($(".btn-next").attr("disabled")).toEqual("disabled");
    }

    function expectNextButtonIsEnabled() {
    	expect($(".btn-next").attr("disabled")).toBeUndefined(); 
    }
    
});
