package utils.engine.data.enums;

import static utils.engine.data.enums.QuestionChoiceValue.JAMAIS;
import static utils.engine.data.enums.QuestionChoiceValue.NE_SAIT_PAS;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.QuestionChoiceValue.PARFOIS;
import static utils.engine.data.enums.QuestionChoiceValue.SOUVENT;
import static utils.engine.data.enums.QuestionType.SIMPLE;

import utils.engine.data.QuestionChoice;

public enum ComplementQuestionDescriptor implements QuestionDescriptor {

	// @formatter:off

	DEMARCHES_EN_LIGNE(
			"Effectuez-vous certaines de vos démarches en ligne (déclaration de revenus, banque en ligne) ?",
			SIMPLE,
			choice("Souvent", SOUVENT),
			choice("Parfois", PARFOIS),
			choice("Jamais", JAMAIS)
			),

	CONSULT_RELEVE_CARRIERE(
		"Avez-vous consulté votre relevé de carrière ?",
			SIMPLE,
			choice("Oui", OUI),
			choice("Non", NON)
	),

	ACCORD_INFO_RELEVE_CARRIERE(
		"Êtes-vous d'accord avec ce relevé de carrière ?",
			SIMPLE,
			choice("Oui", OUI),
			choice("Non", NON),
			choice("Je ne sais pas", NE_SAIT_PAS)
	);

	// @formatter:on

	@SuppressWarnings("unused")
	private final String title;
	@SuppressWarnings("unused")
	private final QuestionType questionType;
	private final QuestionChoice[] questionChoices;

	ComplementQuestionDescriptor(final String title, final QuestionType questionType, final QuestionChoice... questionChoices) {
		this.title = title;
		this.questionType = questionType;
		this.questionChoices = questionChoices;
	}

	public QuestionChoice[] getQuestionChoices() {
		return questionChoices;
	}

	private static QuestionChoice choice(final String text, final QuestionChoiceValue value) {
		return new QuestionChoice(text, value);
	}

}
