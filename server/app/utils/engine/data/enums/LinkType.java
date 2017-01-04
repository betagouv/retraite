package utils.engine.data.enums;

public enum LinkType {

	ADRESSE("enveloppe"),
	SITE("nuage"),
	TELEPHONE("telephone"),
	SECURISE("cadenas"),
	AUCUN("nostyle");
	
	public final String style;
	
	private LinkType(final String style) {
		this.style= style;
	}
	
	
}
