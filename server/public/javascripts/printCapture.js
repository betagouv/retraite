"use strict";

$(function() {
	
		//Créations des destinées à recevoir les images 
		//fait avant la capture sinon pb dans l'ordre d'insertion dû à l'asynchonisme de la capture
		$('.printable').each(function(index) {
			$('#printContent').append('<div id="imgPrint' + index +'"></div>');
		});

	
		$('.printable').each(function(index) {
			
			var zone = $(this);
			html2canvas(zone, {
				onrendered: function(canvas) {		
					//Capture de la zone
					var img = canvas.toDataURL( "image/png" );
					
					//Ajout de l'image dans sa div de destination
					$('#imgPrint'+index).append('<img src="' + img + '" />');
					
					//Suppression de la classe printable
					zone.removeClass('printable');
				},
				letterRendering: true
			});
		});
			
});

