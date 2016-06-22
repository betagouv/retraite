package utils.engine.data;

import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;

public class UserChecklistGenerationData {

	private MonthAndYear dateDepart;
	private String departement;
	private Regime[] regimes;
	private RegimeAligne[] regimesAlignes;

	public boolean isConjointCollaborateur;
	public boolean isNSA;
	public boolean isSA;
	public boolean isCarriereLongue;

	public boolean published;

	public UserChecklistGenerationData(final MonthAndYear dateDepart, final String departement, final Regime[] regimes, final RegimeAligne[] regimesAlignes,
			final boolean published) {
		this.dateDepart = dateDepart;
		this.departement = departement;
		this.regimes = regimes;
		this.regimesAlignes = regimesAlignes;
		this.published = published;
	}

	private UserChecklistGenerationData() {
	}

	public MonthAndYear getDateDepart() {
		return dateDepart;
	}

	public String getDepartement() {
		return departement;
	}

	public Regime[] getRegimes() {
		return regimes;
	}

	public RegimeAligne[] getRegimesAlignes() {
		return regimesAlignes;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder {

		private final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData();

		public UserChecklistGenerationData get() {
			return userChecklistGenerationData;
		}

		public Builder withDateDepart(final MonthAndYear dateDepart) {
			userChecklistGenerationData.dateDepart = dateDepart;
			return this;
		}

		public Builder withDepartement(final String departement) {
			userChecklistGenerationData.departement = departement;
			return this;
		}

		public Builder withRegimes(final Regime... regimes) {
			userChecklistGenerationData.regimes = regimes;
			return this;
		}

		public Builder withSA(final boolean isSA) {
			userChecklistGenerationData.isSA = isSA;
			return this;
		}

		public Builder withNSA(final boolean isNSA) {
			userChecklistGenerationData.isNSA = isNSA;
			return this;
		}

		public Builder withConjointCollaborateur(final boolean isConjointCollaborateur) {
			userChecklistGenerationData.isConjointCollaborateur = isConjointCollaborateur;
			return this;
		}

		public Builder withCarriereLonge(final boolean isCarriereLongue) {
			userChecklistGenerationData.isCarriereLongue = isCarriereLongue;
			return this;
		}

	}

}
