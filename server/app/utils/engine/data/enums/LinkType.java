package utils.engine.data.enums;

public enum LinkType {

	ADRESSE("enveloppe1.png"),
	SITE("nuage.png"),
	TELEPHONE("telephone1.png");
	
	public final String src;
	
	private LinkType(final String src) {
		this.src= src;
	}
	
	
}
