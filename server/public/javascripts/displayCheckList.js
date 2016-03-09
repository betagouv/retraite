"use strict";

$(function() {
	
	$('.hiddeninfos').click(function(event) {
		event.preventDefault();
		$(this).removeClass('hiddeninfos');
	});
	
	$('a[href^="#"]').click(function(event){
		event.preventDefault();
		var id = $(this).attr("href");
		var margin = $('body').css('padding-top').replace('px','');
		var top = $(id).offset().top - margin;
		$('html, body').animate({
			scrollTop: top
		}, {
			duration: 1200
		});
	});
	
	$('button.record').click(function(event) {
		event.preventDefault();
		$("form").attr("action", "/application/pdf");
		$("form").submit();
	});
	
	$('button.mail').click(function(event) {
		event.preventDefault();
		$('#DialogSendEMail').modal();
	});
	
	$('#validSendMail').click(function(event) {
		event.preventDefault();
		if (isEMailProvided()) {
			$('#DialogSendEMail').modal('hide');
			sendRequestToSendByEMail();
		} else {
			$("#divEmail div").addClass('has-error');
			$("#emailMissing").removeClass('hidden');
		}
	});
	
	function isEMailProvided() {
		var $emailInput = $("#email");
		var value = $emailInput.val();
		return value && value.length > 5 && value.indexOf("@") > 0;
	}
	
	function sendRequestToSendByEMail() {
		$.ajax({
			url: '/application/sendmail',
			type: 'post',
			dataType: 'json',
			data: $("form").serialize(),
			success: function(data) {
			}
		});
	}
});