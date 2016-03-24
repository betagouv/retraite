"use strict";

var RetraiteGetUserData = {};

(function() {

	function setInputMasks() {
		$("#naissance").mask("99/99/9999");
		$("#nir").mask("9 99 99 99 999 999? 99");
	}
	
	function addKeyEvents() { 
		$("#nom,#naissance,#nir").keyup(function(event) {
			updateNextButtonState();
		});
	}
	
	function initJquery() {
		setInputMasks();
		addKeyEvents();
		updateNextButtonState();
	}

	var fillForm = function(nom, nir) {
		$('#nom').val(nom);
		$('#naissance').val("17/11/1954");
		$('#nir').val(nir);
		updateNextButtonState();
	};
	
	function updateNextButtonState() {
		var $nextButton = $(".btn-next");
		if (areAllFieldsFilled()) {
			$nextButton.removeAttr("disabled");
		} else {
			$nextButton.attr("disabled","disabled");			
		}
		//$(document).trigger('Retraite:getUserData:diplayUpdated');
	}
	
	function areAllFieldsFilled() {
		//console.log($('#nom').val()+" , "+$('#naissance').val()+" , "+$('#nir').val());
		return isLengthMin('nom', 2) && isLength('naissance', 10, true) && (isLength('nir', 13, true) || isLength('nir', 15, true));
	}
	
	function isLengthMin(id, expected) {
		var val = $('#'+id).val();
		return val && val.length >= expected;
	}
	
	function isLength(id, expected, ignoreSpacesAndMask) {
		var val = $('#'+id).val();
		if (ignoreSpacesAndMask) {
			val = val.replace(/ /g, '').replace(/_/g, '');
		}
		return val.length === expected;
	}
	
	RetraiteGetUserData.initJquery = initJquery;
	RetraiteGetUserData.fillForm = fillForm;

})();

$(RetraiteGetUserData.initJquery);
