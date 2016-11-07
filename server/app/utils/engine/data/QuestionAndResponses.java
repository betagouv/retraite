package utils.engine.data;

import java.util.List;

import utils.engine.data.enums.RegimeAligne;

public class QuestionAndResponses {

	public final String question;
	public final List<String> responses;
	public final RegimeAligne liquidateur;

	public QuestionAndResponses(final String question, final List<String> responses, final RegimeAligne liquidateur) {
		this.question = question;
		this.responses = responses;
		this.liquidateur = liquidateur;
	}

}