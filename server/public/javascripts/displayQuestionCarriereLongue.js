"use strict";

var DisplayQuestionCarriereLongue = {};

(function() {
	
	function updateNextButtonState() {
		var checked = $('input[name=CARRIERE_LONGUE]').is(':checked');
		var $nextButton = $(".btn-next");
		if (checked) {
			$nextButton.removeAttr("disabled");
		} else {
			$nextButton.attr("disabled","disabled");			
		}
	}
		
	function updateDisplay() {
		updateNextButtonState();
		$(document).trigger('DisplayQuestionCarriereLongue:diplayUpdated');
	}
	
	function initJquery() {
		$('input[name=CARRIERE_LONGUE]').click(function() {
			// Il faut désynchroniser pour être sûr que les états 'checked' soient fixés (notamment dans le cadre des TU)
			setTimeout(updateDisplay, 0);
		});
		
		updateDisplay();
	}

	DisplayQuestionCarriereLongue.initJquery = initJquery;

	
})();

$(DisplayQuestionCarriereLongue.initJquery);
