package utils;

import models.Delai;
import models.Delai.Type;
import models.Delai.Unite;

public class TestsUtilsDelai {

	public static Delai createDelaiAucun() {
		return createDelai(Delai.Type.AUCUN);
	}

	public static Delai createDelaiDesQuePossible() {
		return createDelai(Delai.Type.DESQUE);
	}

	public static Delai createDelaiAuPlus(final int min, final Unite unite) {
		final Delai delai = createDelai(Delai.Type.AUPLUS);
		delai.min = min;
		delai.unite = unite;
		return delai;
	}

	public static Delai createDelaiEntre(final int min, final int max, final Unite unite) {
		final Delai delai = createDelai(Delai.Type.ENTRE);
		delai.min = min;
		delai.max = max;
		delai.unite = unite;
		return delai;
	}

	public static Delai createDelaiAPartir(final int min, final Unite unite) {
		final Delai delai = createDelai(Delai.Type.APARTIR);
		delai.min = min;
		delai.unite = unite;
		return delai;
	}

	public static Delai createDelai(final Type type) {
		final Delai delai = new Delai();
		delai.type = type;
		return delai;
	}

}
