package utils.engine.data.enums;

import static utils.engine.data.enums.ChecklistSelector.SELECT_CNAV;
import static utils.engine.data.enums.ChecklistSelector.SELECT_CNAV_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE;
import static utils.engine.data.enums.ChecklistSelector.SELECT_MSA;
import static utils.engine.data.enums.ChecklistSelector.SELECT_MSA_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE;
import static utils.engine.data.enums.ChecklistSelector.SELECT_RSI;
import static utils.engine.data.enums.ChecklistSelector.SELECT_RSI_AVANT_73;
import static utils.engine.data.enums.ChecklistSelector.SELECT_RSI_INVALIDITE;
import static utils.engine.data.enums.QuestionChoiceValue.AUTRE;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.HORS_TERRITOIRE_FRANCAIS;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_AVANT_73;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.MSA;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.RSI;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionType.MULTIPLE;
import static utils.engine.data.enums.QuestionType.SIMPLE;

import utils.engine.data.QuestionChoice;

public enum LiquidateurQuestionDescriptor implements QuestionDescriptor {

	// @formatter:off

	CHEF_EXPLOITATION_AGRICOLE(
		"Avez-vous été chef d'exploitation ou d'entreprise agricole au cours de votre carrière ?",
			SIMPLE,
			choice("Oui", OUI, SELECT_MSA),
			choice("Non", NON)
	),

	DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE(
		"Quelle est votre activité actuelle ou la dernière activité que vous avez exercée ?",
			SIMPLE,
			choice("Salarié agricole", SA, SELECT_MSA),
			choice("Conjoint collaborateur/aide familial d'un exploitant agricole", CONJOINT, SELECT_MSA),
			choice("Autre (salariés hors agricole, agents non titulaires du public, etc.)", AUTRE, SELECT_CNAV),
			choice("Deux de ces activités en même temps", DEUX_ACTIVITES)
	),

	ORGA_FRAIS_SANTE_CPAM_MSA(
		"Quel est l'organisme qui vous rembourse vos frais de santé ?",
			SIMPLE,
			choice("CPAM/ Assurance maladie", CPAM, SELECT_CNAV),
			choice("MSA", MSA, SELECT_MSA)
	),

	ORGA_FRAIS_SANTE_CPAM_RSI(
			"Quel est l'organisme qui vous rembourse vos frais de santé ?",
			SIMPLE,
			choice("CPAM", CPAM, SELECT_CNAV),
			choice("RSI", RSI, SELECT_RSI)
			),

	DERN_ACT_INDEP_CONJOINT_AUTRE(
		"Quelle est votre activité actuelle ou la dernière activité que vous avez exercée ?",
			SIMPLE,
			choice("Indépendant (artisan ou commerçant)", INDEP, SELECT_RSI),
			choice("Conjoint collaborateur", CONJOINT, SELECT_RSI),
			choice("Autre (salariés hors agricole, agents non titulaires du public, etc.)", AUTRE, SELECT_CNAV),
			choice("Deux activités en même temps", DEUX_ACTIVITES)
	),

	DIVERSES_SITUATIONS_CNAV_RSI(
		"Êtes-vous dans l'une ou plusieurs des situations suivantes ?",
			MULTIPLE,
			choice("J'ai exercé une activité d'indépendant avant 1973", INDEP_AVANT_73, SELECT_RSI_AVANT_73),
			choice("Je reçois une pension d'invalidité versée par le RSI", INVALIDITE_RSI, SELECT_RSI),
			choice("Je souhaite bénéficier du dispositif de retraite pour pénibilité", PENIBILITE, SELECT_CNAV_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE),
			choice("J'ai effectué une partie de ma carrière en dehors du territoire français", HORS_TERRITOIRE_FRANCAIS, SELECT_CNAV_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE),
			choice("Autre situation", AUTRE)
	),

	DIVERSES_SITUATIONS_RSI_MSA(
		"Êtes-vous dans l'une ou plusieurs des situations suivantes ?",
			MULTIPLE,
			choice("J'ai exercé une activité d'indépendant (artisan ou commerçant) avant 1973", INDEP_AVANT_73, SELECT_RSI_AVANT_73),
			choice("Je reçois une pension d'invalidité versée par le RSI", INVALIDITE_RSI, SELECT_RSI_INVALIDITE),
			choice("Je souhaite bénéficier du dispositif de retraite pour pénibilité", PENIBILITE, SELECT_MSA_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE),
			choice("J'ai effectué une partie de ma carrière en dehors du territoire français", HORS_TERRITOIRE_FRANCAIS, SELECT_MSA_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE),
			choice("Autre situation", AUTRE)
			),

	DERN_ACT_NSA_CONJOINT_INDEP(
		"Quelle est votre dernière activité exercée ?",
			SIMPLE,
			choice("Chef d'exploitation ou d'entreprise agricole", NSA, SELECT_MSA),
			choice("Salarié agricole", SA, SELECT_MSA),
			choice("Conjoint collaborateur/aide familial d'un exploitant agricole", CONJOINT, SELECT_MSA),
			choice("Indépendant (artisan ou commerçant)", INDEP, SELECT_RSI),
			choice("Conjoint collaborateur d'un indépendant (artisan ou commerçant)", CONJOINT_INDEP, SELECT_RSI),
			choice("Deux de ces activités en même temps", DEUX_ACTIVITES)
	),

	ORGA_FRAIS_SANTE_RSI_MSA(
		"Quel est l'organisme qui vous rembourse vos frais de santé ?",
			SIMPLE,
			choice("RSI", RSI, SELECT_RSI),
			choice("MSA", MSA, SELECT_MSA)
			);

	// @formatter:on

	@SuppressWarnings("unused")
	private final String title;
	@SuppressWarnings("unused")
	private final QuestionType questionType;
	private final QuestionChoice[] questionChoices;

	LiquidateurQuestionDescriptor(final String title, final QuestionType questionType, final QuestionChoice... questionChoices) {
		this.title = title;
		this.questionType = questionType;
		this.questionChoices = questionChoices;
	}

	public QuestionChoice[] getQuestionChoices() {
		return questionChoices;
	}

	private static QuestionChoice choice(final String text, final QuestionChoiceValue value) {
		return choice(text, value, null);
	}

	private static QuestionChoice choice(final String text, final QuestionChoiceValue value, final ChecklistSelector selector) {
		return new QuestionChoice(text, value, selector);
	}

}
