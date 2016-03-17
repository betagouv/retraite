"use strict";

var RetraiteQuestions = {};

(function() {
		
	function areAllVisibleQuestionWithAnswer() {
		var areAllVisibleQuestionWithAnswer = true;
		$('div.question:visible').each(function() {
			var $divQuestion = $(this);
			var $inputsChecked = $divQuestion.find('input:checked');
			if ($inputsChecked.length === 0) {
				areAllVisibleQuestionWithAnswer = false;
			}
		});
		return areAllVisibleQuestionWithAnswer; 
	}
	
	var updateHiddenDataForResponses = function() {
		var $divQuestion = $('div.question');
		var $inputsChecked = $divQuestion.find('input:checked');
		var currentValues = [];
		$inputsChecked.each(function() {
			var value = $(this).val();
			currentValues.push(value);				
		});
		$("input#reponseJsonStr").val(JSON.stringify(currentValues));
		
	};
	
	function updateNextButtonState() {
		var $nextButton = $(".btn-next");
		if (areAllVisibleQuestionWithAnswer()) {
			$nextButton.removeAttr("disabled");
		} else {
			$nextButton.attr("disabled","disabled");			
		}
	}
		
	function updateDisplayAndData() {
		updateHiddenDataForResponses();
		updateNextButtonState();
		$(document).trigger('Retraite:questions:diplayUpdated');
	}
	
	function initJquery() {
		$('input.questions-choice').click(function() {
			// Il faut désynchroniser pour être sûr que les états 'checked' soient fixés (notamment dans le cadre des TU)
			setTimeout(updateDisplayAndData, 0);
		});
		
		updateDisplayAndData();
	}

	RetraiteQuestions.areAllVisibleQuestionWithAnswer = areAllVisibleQuestionWithAnswer;
	RetraiteQuestions.initJquery = initJquery;

})();

$(RetraiteQuestions.initJquery);

