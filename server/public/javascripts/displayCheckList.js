"use strict";

$(function() {
	
	var regimeLiquidateur = $("[name='postData.hidden_liquidateur']").val();
	
	sendGoogleAnalyticsEvent('Checklist', 'display', regimeLiquidateur);
	
	addScrollHandlerToSendEventIfScrollToBottom();
	
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
		sendGoogleAnalyticsEvent('Checklist', 'imprimer/PDF', regimeLiquidateur);
		$("form").attr("action", "/application/pdf");
		$("form").submit();
	});
	
	$('button.mail').click(function(event) {
		event.preventDefault();
		sendGoogleAnalyticsEvent('Checklist', 'clickMail', regimeLiquidateur);
		$('#DialogSendEMail').modal();
	});
	
	$('button.print').click(function(event) {
		event.preventDefault();
		sendGoogleAnalyticsEvent('Checklist', 'print', regimeLiquidateur);
		window.print();
		/*$("form").attr("action", "/application/pdf");
		$("form").submit();*/
	});
	
	/* Plus d'envoi de mail pour l'instant
	
	$('#validSendMail').click(function(event) {
		event.preventDefault();
		if (isEMailProvided()) {
			$('#DialogSendEMail').modal('hide');
			sendGoogleAnalyticsEvent('Checklist', 'sendMail', regimeLiquidateur);
			sendRequestToSendByEMail();
		} else {
			$("#divEmail div").addClass('has-error');
			$("#emailMissing").removeClass('hidden');
		}
	});*/
	
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
	
	function addScrollHandlerToSendEventIfScrollToBottom() {
		
		var eventSent = false;
		
		function isScrolledToBottom() {
			return (window.scrollY + window.innerHeight) >= (document.body.offsetHeight - window.innerHeight);
		};
		
		$(window).scroll(function(event) {
		    if (!eventSent && isScrolledToBottom()) {
		    	eventSent = true;
		    	sendGoogleAnalyticsEvent('Checklist', 'scrolledToBottom', regimeLiquidateur);
		    }
		});
	}
	
	// Gestion des événements d'impression
	
	var beforePrint = function() {
    	sendGoogleAnalyticsEvent('Checklist', 'printDirectFromBrowser', regimeLiquidateur);
    };
    var afterPrint = function() {
    };

    if (window.matchMedia) {
        var mediaQueryList = window.matchMedia('print');
        mediaQueryList.addListener(function(mql) {
            if (mql.matches) {
                beforePrint();
            } else {
                afterPrint();
            }
        });
    }

    window.onbeforeprint = beforePrint;
    window.onafterprint = afterPrint;
	
});