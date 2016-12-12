package utils.engine.data.enums;

public enum LinkType {

	ADRESSE("enveloppe"),
	SITE("nuage"),
	TELEPHONE("telephone");
	
	public final String style;
	
	private LinkType(final String style) {
		this.style= style;
	}
	
	
}
