title:	        '.contenu h2',
// @Matti : j'ai réussi à trouver cette solution pour récupérer le select/option qui contient le texte "2018", y'a mieux ?
year2018Option: { xpath: '//option[contains(text(),"2018")]' },
    
setYear2018: function () {
	return	this.year2018()();
}

/* @Matti : 
selectYear: function(year) {
    comment chercher ici le select/option qui contient le texte ==year et le sélectionner ?
}
*/