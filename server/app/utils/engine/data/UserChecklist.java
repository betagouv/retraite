package utils.engine.data;

import java.util.List;

import models.Caisse;
import utils.engine.data.enums.Regime;

public class UserChecklist {

	public String nom;

	public Caisse caisse;

	public Regime[] autresRegimesDeBase;

	public Regime[] regimesComplementaires;

	public List<UserChapitre> chapitres;

}
