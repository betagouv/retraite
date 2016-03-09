// Apparemment, 'name' pose problème... cf Matti ?

fieldName:		'#nom',
fieldNaissance:	'#naissance',
fieldNir:		'#nir',
nextStepButton:	'.btn-next',

fill: function (name, naissance, nir) {
	return	this.setFieldName(name)()
                .then(this.setFieldNaissance(naissance))
                .then(this.setFieldNir(nir));
}
