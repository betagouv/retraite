package utils.dao;

import java.util.List;

import models.Caisse;
import models.CaisseDepartementale;

public class CaisseDepartementaleDao {

	public void deleteDepartement(final Long caisseId, final String departement) {
		final Caisse caisse = Caisse.findById(caisseId);
		if (caisse == null) {
			throw new IllegalStateException(
					"Impossible de trouver une caisse pour la caisse d'ID " + caisseId);
		}
		final CaisseDepartementale caisseDepartementale = CaisseDepartementale.find("byCaisseAndDepartement", caisse, departement).first();
		if (caisseDepartementale == null) {
			throw new IllegalStateException(
					"Impossible de trouver une CaisseDepartementale pour la caisse d'ID " + caisseId + " et le département " + departement
							+ ". La caisse concernée est '" + caisse.nom + "'");
		}
		System.out.println("caisseDepartementale = " + caisseDepartementale);
		System.out.println("avant : " + CaisseDepartementale.find("byCaisse", caisse).fetch());
		caisseDepartementale.delete();
		System.out.println("apres : " + CaisseDepartementale.find("byCaisse", caisse).fetch());
		deleteCaisseIfNotMoreUsed(caisse);
	}

	private void deleteCaisseIfNotMoreUsed(final Caisse caisse) {
		final List<CaisseDepartementale> caissesDepartementales = CaisseDepartementale.find("byCaisse", caisse).fetch();
		System.out.println(caissesDepartementales.size());
		if (caissesDepartementales.isEmpty()) {
			caisse.delete();
		}
	}

}
