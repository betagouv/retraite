package utils.engine.data.enums;

import utils.engine.utils.RetraitePropertiesLoader;

public enum RegimeAligne {

	// @formatter:off

	CNAV,
	MSA,
	RSI;
	

	public final String urlAgeDepart;
	public final String urlDroits;
	public final String urlSimulMontant;
	public final String urlDispositifDepartAvantAgeLegal;
	public final String urlInfosPenibilite;
	public final String urlInfosDepartRetraite;

	private RegimeAligne() {
		
		RetraitePropertiesLoader propertyLoader =  RetraitePropertiesLoader.getInstance();
		
		this.urlAgeDepart = propertyLoader.getProperty(this.name() + ".urlAgeDepart");
		this.urlDroits = propertyLoader.getProperty(this.name() + ".urlDroits");
		this.urlSimulMontant = propertyLoader.getProperty(this.name() + ".urlSimulMontant");
		this.urlDispositifDepartAvantAgeLegal = propertyLoader.getProperty(this.name() + ".urlDispositifDepartAvantAgeLegal");
		this.urlInfosPenibilite = propertyLoader.getProperty(this.name() + ".urlInfosPenibilite");
		this.urlInfosDepartRetraite = propertyLoader.getProperty(this.name() + ".urlInfosDepartRetraite");
	}

}
