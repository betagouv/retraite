'use strict';

describe('displaySortieQuestionCarriereLongue', function () {

	beforeEach(function () {
		jasmine.getFixtures().fixturesPath = 'base/testjs/spec/javascripts/fixtures';
		loadFixtures('displaySortieQuestionCarriereLongue.html');
		displaySortieQuestionCarriereLongue.initJquery(); 
	});

    it('should be disabled initialy', function () {
    	expectNextButtonIsDisabled();
    });
    
    it('should be enabled after input checkbox is clicked', function (done) {
    	
    	$('[name=CARRIERE_LONGUE]').trigger( "click" );
		
    	$(document).on('displaySortieQuestionCarriereLongue:diplayUpdated', function() {
			// Il faut se désabonner de l'évènement pour éviter le mélange entre chaque TU
			$(document).off('displaySortieQuestionCarriereLongue:diplayUpdated');
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
