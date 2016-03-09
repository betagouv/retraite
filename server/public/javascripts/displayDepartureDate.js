"use strict";

$(function() {
	
	$("input[name='postData.departInconnu']").click(function() {
		var departInconnu = $(this).val();
		var $departMoisSelect = $("[name='postData.departMois']");
		var $departAnneeSelect = $("[name='postData.departAnnee']");
		$departMoisSelect.prop('disabled', departInconnu == "true");
		$departAnneeSelect.prop('disabled', departInconnu == "true");
	});
});