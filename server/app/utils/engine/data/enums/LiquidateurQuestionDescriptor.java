package utils.engine.data.enums;

import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_C;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_D;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_AVANT_73;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_RSI;
import static utils.engine.data.enums.QuestionMandatoryOrOptionnal.MANDATORY;
import static utils.engine.data.enums.QuestionMandatoryOrOptionnal.OPTIONNAL;
import static utils.engine.data.enums.QuestionType.MULTIPLE;
import static utils.engine.data.enums.QuestionType.SIMPLE;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.EngineUtils.contains;

import java.util.ArrayList;
import java.util.List;

import controllers.data.PostData;
import utils.engine.data.CommonExchangeData;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RenderData;

public enum LiquidateurQuestionDescriptor {

	// @formatter:off

	QUESTION_A(
		"Au cours de votre carrière, avez-vous été ?",
			SIMPLE,
			MANDATORY,
			choice("Chef d'exploitation ou d'entreprise agricole", NSA),
			choice("Salarié agricole", SA),
			choice("Les deux", DEUX_ACTIVITES)
	),

	QUESTION_B(
		"Quelle est votre activité actuelle ou la dernière activité que vous avez exercée ?",
			SIMPLE,
			MANDATORY,
			choice("Salarié (non agricole)", SALARIE),
			choice("Chef d'exploitation", NSA),
			choice("Salarié agricole", SA),
			choice("Artisan ou commerçant", INDEP),
			choice("Conjoint collaborateur d'un artisan commerçant", CONJOINT_INDEP),
			choice("Deux de ces activités en même temps", DEUX_ACTIVITES)
	),

	QUESTION_C(
		"Êtes-vous dans l'une ou plusieurs des situations suivantes ?",
			MULTIPLE,
			OPTIONNAL,
			choice("J'ai exercé une activité d'indépendant avant 1973", INDEP_AVANT_73),
			choice("Je reçois une pension d'invalidité versée par le RSI", INVALIDITE_RSI),
			choice("Je souhaite bénéficier du dispositif de retraite pour pénibilité", PENIBILITE)
	),

	QUESTION_D(
		"Quel est l'organisme qui vous rembourse vos fais de santé ?",
			SIMPLE,
			MANDATORY,
			choice("CPAM", SANTE_CPAM),
			choice("MSA", SANTE_MSA),
			choice("RSI", SANTE_RSI)
	),

	QUESTION_E(
		"Etes-vous actuellement chef d'exploitation ou d'entreprise agricole ?",
			SIMPLE,
			MANDATORY,
			choice("Oui", OUI),
			choice("Non", NON)
	),

	QUESTION_F(
		"Etes-vous actuellement chef d'entreprise ?",
			SIMPLE,
			MANDATORY,
			choice("Oui", OUI),
			choice("Non", NON)
	);

	// @formatter:on

	private final String title;
	@SuppressWarnings("unused")
	private final QuestionType questionType;
	@SuppressWarnings("unused")
	private final QuestionMandatoryOrOptionnal questionMandatoryOrOptionnal;
	private final QuestionChoice[] questionChoices;

	LiquidateurQuestionDescriptor(final String title, final QuestionType questionType, final QuestionMandatoryOrOptionnal questionMandatoryOrOptionnal,
			final QuestionChoice... questionChoices) {
		this.title = title;
		this.questionType = questionType;
		this.questionMandatoryOrOptionnal = questionMandatoryOrOptionnal;
		this.questionChoices = questionChoices;
	}

	public String getTitle() {
		return title;
	}

	public QuestionMandatoryOrOptionnal getQuestionMandatoryOrOptionnal() {
		return questionMandatoryOrOptionnal;
	}

	public QuestionChoice[] getQuestionChoices() {
		return questionChoices;
	}

	private static QuestionChoice choice(final String text, final QuestionChoiceValue value) {
		return new QuestionChoice(text, value);
	}

	public boolean isLast() {
		final LiquidateurQuestionDescriptor[] values = values();
		return this == values[values.length - 1];
	}

	public boolean isBefore(final LiquidateurQuestionDescriptor otherStep) {
		final LiquidateurQuestionDescriptor[] values = values();
		for (final LiquidateurQuestionDescriptor desc : values) {
			if (desc == otherStep) {
				return false;
			}
			if (desc == this) {
				return true;
			}
		}
		return true;
	}

	public QuestionChoice getChoice(final QuestionChoiceValue value) {
		for (final QuestionChoice questionChoice : questionChoices) {
			if (questionChoice.getValue() == value) {
				return questionChoice;
			}
		}
		return null;
	}
}
