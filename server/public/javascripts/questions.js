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
	
	var updateDisplayQuestions = function() {
		
		var currentValues = {};
		
		var mustShowQuestion = function($divQuestion) {
			var key = $divQuestion.attr('condition-key');
			var value = $divQuestion.attr('condition-value');
			if (key && value) {
				if ($.inArray(value, currentValues[key]) != -1) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		};

		var storeValuesForThisQuestion = function($divQuestion) {
			var $inputsChecked = $divQuestion.find('input:checked');
			$inputsChecked.each(function() {
				var key = $(this).attr('condition-key');
				var value = $(this).val();
				currentValues[key] = currentValues[key] || [];				
				currentValues[key].push(value);				
			});
			$("input#reponseJsonStr").val(JSON.stringify(currentValues));
		};
		
		$('div.question').each(function() {
			var $divQuestion = $(this);
			if (mustShowQuestion($divQuestion)) {
				$divQuestion.show();
				storeValuesForThisQuestion($divQuestion);
			} else {
				$divQuestion.hide();
			}
		});
		
	};
	
	function updateNextButtonState() {
		var $nextButton = $(".btn-next");
		if (areAllVisibleQuestionWithAnswer()) {
			$nextButton.removeAttr("disabled");
		} else {
			$nextButton.attr("disabled","disabled");			
		}
	}
		
	function updateDisplay() {
		updateDisplayQuestions();
		updateNextButtonState();
		$(document).trigger('Retraite:questions:diplayUpdated');
	}
	
	function initJquery() {
		$('input.questions-choice').click(function() {
			// Il faut désynchroniser pour être sûr que les états 'checked' soient fixés (notamment dans le cadre des TU)
			setTimeout(updateDisplay, 0);
		});
		
		updateDisplay();
	}

	RetraiteQuestions.areAllVisibleQuestionWithAnswer = areAllVisibleQuestionWithAnswer;
	RetraiteQuestions.initJquery = initJquery;

})();

$(RetraiteQuestions.initJquery);

