package utils.engine.data;

import java.util.List;

public class QuestionAndResponses {

	public final String question;
	public final List<String> responses;

	public QuestionAndResponses(final String question, final List<String> responses) {
		this.question = question;
		this.responses = responses;
	}

}