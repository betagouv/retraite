function sendGoogleAnalyticsEvent(category, action, label, value) {
	
	if (typeof ga == "undefined") return;
	
	var options = {
		hitType: 'event',
		eventCategory: category,
		eventAction: action
	};
	if (label) {
		options.eventLabel = label;
		if (value) {
			options.eventValue = value;
		}
	}
	ga('send', options);		
}
