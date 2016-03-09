'use strict';


describe('questions', function () {

    describe('RetraiteQuestions.areAllVisibleQuestionWithAnswer()', function () {
        
    	beforeEach(function () {
    		jasmine.getFixtures().fixturesPath = 'base/testjs/spec/javascripts/fixtures';
    		loadFixtures('questions.html'); 
    		RetraiteQuestions.initJquery(); 
    	});

        it('should be disabled initialy', function () {
        	expectNextButtonIsDisabled();
        });
        
        it('should be enabled if question 1 is checked without other questions visible', function (done) {
        	
        	$('[value=INDEP_AVANT_73]').trigger( "click" );
    		
        	$(document).on('Retraite:questions:diplayUpdated', function() {
    			// Il faut se désabonner de l'évènement pour éviter le mélange entre chaque TU
    			$(document).off('Retraite:questions:diplayUpdated');
    			expectNextButtonIsEnabled(); 
    			done();
    		}); 
        });
        
        it('should be disabled if question 1 is checked and question 2 is visible and not checked', function (done) {
        	
        	$('[value=AUCUNE]').trigger( "click" );
        	
        	$(document).on('Retraite:questions:diplayUpdated', function() {
        		$(document).off('Retraite:questions:diplayUpdated');
        		expectNextButtonIsDisabled(); 
        		done();
        	}); 
        });
        
        it('should be enabled if questions 1 and 2 are checked', function (done) { 
        	
        	$('[value=AUCUNE]').trigger("click");
    		$('[value=SA]').trigger("click");
    		
    		$(document).on('Retraite:questions:diplayUpdated', function() {
    			$(document).off('Retraite:questions:diplayUpdated');
    			expectNextButtonIsEnabled(); 
    			done();
    		});
        });
        
    });
    
    function expectNextButtonIsDisabled() {
    	expect($(".btn-next").attr("disabled")).toEqual("disabled");
    }

    function expectNextButtonIsEnabled() {
    	expect($(".btn-next").attr("disabled")).toBeUndefined(); 
    }
    
});
