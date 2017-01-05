package utils.engine.data.enums;

import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_OLD;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_AVANT_73;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.NSA_OLD;
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

import utils.engine.data.QuestionChoice;

public enum LiquidateurQuestionDescriptor {

	// @formatter:off

	QUESTION_A(
		"Au cours de votre carrière, avez-vous été&#8239?",
		"Professions exercées durant votre carrière&#8239:",
			SIMPLE,
			MANDATORY,
			choice("Chef d'exploitation ou d'entreprise agricole", NSA),
			choice("Salarié agricole", SA),
			choice("Les deux", "Salarié agricole et chef d'exploitation ou d'entreprise agricole", DEUX_ACTIVITES)
	),

	QUESTION_B(
		"Quelle est votre activité actuelle ou la dernière activité que vous avez exercée&#8239?",
		"Activité actuelle ou dernière activité exercée&#8239:",
			SIMPLE,
			MANDATORY,
			choice("Salarié (non agricole)", SALARIE),
			choice("Chef d'exploitation ou d'entreprise agricole (en activité)", NSA),
			choice("Chef d'exploitation ou d'entreprise agricole (plus en activité)", NSA_OLD),
			choice("Salarié agricole", SA),
			choice("Chef d’entreprise indépendant (en activité)", INDEP),
			choice("Chef d’entreprise indépendant (plus en activité)", INDEP_OLD),
			choice("Conjoint(e) collaborateur(trice) d'un(e) chef d’entreprise indépendant(e)", CONJOINT_INDEP),
			choice("Deux de ces activités en même temps", "Deux activités en même temps (poly-activité)", DEUX_ACTIVITES)
	),

	QUESTION_C(
		"Votre situation :",
		"",
			MULTIPLE,
			OPTIONNAL,
			choice("J'ai exercé une activité d'indépendant avant 1973 :", "J'ai exercé une activité d'indépendant avant 1973.", INDEP_AVANT_73),
			choice("Je reçois une pension d'invalidité versée par le RSI :", "Je reçois une pension d'invalidité versée par le RSI.", INVALIDITE_RSI),
			choice("Je souhaite bénéficier du dispositif de retraite pour pénibilité :", "Je souhaite bénéficier du dispositif de retraite pour pénibilité.", PENIBILITE)
	),

	QUESTION_D(
		"Quel est l'organisme qui vous rembourse vos fais de santé&#8239?",
		"",
			SIMPLE,
			MANDATORY,
			choice("CPAM", "L'organisme qui rembourse mes frais de santé est la CPAM.", SANTE_CPAM),
			choice("MSA", "L'organisme qui rembourse mes frais de santé est la MSA.", SANTE_MSA),
			choice("RSI", "L'organisme qui rembourse mes frais de santé est le RSI.", SANTE_RSI)
	),

	QUESTION_E(
		"Etes-vous actuellement chef d'exploitation ou d'entreprise agricole&#8239?",
		"",
			SIMPLE,
			MANDATORY,
			choice("Oui", "Actuellement, je suis chef d'exploitation ou d'entreprise agricole.", OUI),
			choice("Non", "Actuellement, je ne suis pas chef d'exploitation ou d'entreprise agricole.", NON)
	),

	QUESTION_F(
		"Etes-vous actuellement chef d'entreprise&#8239?",
		"",
			SIMPLE,
			MANDATORY,
			choice("Oui", "Actuellement, je suis chef d'entreprise.", OUI),
			choice("Non", "Actuellement, je ne suis pas chef d'entreprise.", NON)
	);

	// @formatter:on

	private final String title;
	private final String titleChecklist;
	@SuppressWarnings("unused")
	private final QuestionType questionType;
	@SuppressWarnings("unused")
	private final QuestionMandatoryOrOptionnal questionMandatoryOrOptionnal;
	private final QuestionChoice[] questionChoices;
		
	LiquidateurQuestionDescriptor(final String title, final String titleChecklist, final QuestionType questionType, final QuestionMandatoryOrOptionnal questionMandatoryOrOptionnal,
			final QuestionChoice... questionChoices) {
		this.title = title;
		this.titleChecklist = titleChecklist;
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

	private static QuestionChoice choice(final String text, final String textChecklist, final QuestionChoiceValue value) {
		return new QuestionChoice(text, textChecklist, value);
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
