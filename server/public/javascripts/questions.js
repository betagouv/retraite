"use strict";

var RetraiteQuestions = {};

(function() {
		
	var $divQuestion, $nextButton, defaultNextButtonText;
	
	function questionIsOptionnal() {
		return $divQuestion.data('optionnal');
	}
	
	function noAnswerChecked() {
		var $inputsChecked = $divQuestion.find('input:checked');
		return ($inputsChecked.length === 0); 
	}
	
	var updateHiddenDataForResponses = function() {
		var $inputsChecked = $divQuestion.find('input:checked');
		var currentValues = [];
		$inputsChecked.each(function() {
			var value = $(this).val();
			currentValues.push(value);				
		});
		$("input#reponseJsonStr").val(JSON.stringify(currentValues));
		
	};
	
	function enableNextButton() {
		$nextButton.removeAttr("disabled");
	}
	
	function disableNextButton() {
		$nextButton.attr("disabled","disabled");	
	}
	
	function updateNextButtonState() {
		if (questionIsOptionnal()) {
			if (noAnswerChecked()) {
				$nextButton.text("Aucune de ces situations");
			} else {
				$nextButton.text(defaultNextButtonText);
			}
			enableNextButton();
		} else {
			if (noAnswerChecked()) {
				disableNextButton();			
			} else {
				enableNextButton();
			}
		}
	}
		
	function updateDisplayAndData() {
		updateHiddenDataForResponses();
		updateNextButtonState();
		$(document).trigger('Retraite:questions:diplayUpdated');
	}
	
	function initJquery() {

		$divQuestion = $('div.question');
		$nextButton = $(".btn-next");
		defaultNextButtonText = $nextButton.text();

		$('input.questions-choice').click(function() {
			// Il faut désynchroniser pour être sûr que les états 'checked' soient fixés (notamment dans le cadre des TU)
			setTimeout(updateDisplayAndData, 0);
		});
		
		updateDisplayAndData();
	}

	RetraiteQuestions.initJquery = initJquery;

})();

$(RetraiteQuestions.initJquery);

