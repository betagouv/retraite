package utils.engine.data;

public class ConditionDelai {

	private final String plusOuMoins;
	private final String nombre;
	private final String unite;

	public ConditionDelai(final String plusOuMoins, final String nombre, final String unite) {
		this.plusOuMoins = plusOuMoins;
		this.nombre = nombre;
		this.unite = unite;
	}

	public String getPlusOuMoins() {
		return plusOuMoins;
	}

	public String getNombre() {
		return nombre;
	}

	public String getUnite() {
		return unite;
	}

}
