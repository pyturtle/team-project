package entity.subgoal;

import java.util.UUID;

/**
 * SubgoalQuestionAnswerBuilder constructs SubgoalQuestionAnswer objects
 * using a fluent builder pattern. It allows setting fields individually
 * or generating a unique ID automatically.
 */
public class SubgoalQuestionAnswerBuilder {
    private String id = "";
    private String subgoalId = "";
    private String questionMessage = "";
    private String responseMessage = "";

    /**
     * Generates a new unique ID for the question–answer entry.
     * @return this builder with an assigned generated ID
     */
    public SubgoalQuestionAnswerBuilder generateId() {
        this.id = UUID.randomUUID().toString();
        return this;
    }

    /**
     * Sets the ID of the question–answer entry.
     * @param id the ID to set
     * @return this builder
     */
    public SubgoalQuestionAnswerBuilder setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the associated subgoal ID.
     * @param subgoalId the ID of the subgoal this entry belongs to
     * @return this builder
     */
    public SubgoalQuestionAnswerBuilder setSubgoalId(String subgoalId) {
        this.subgoalId = subgoalId;
        return this;
    }

    /**
     * Sets the user's question message.
     * @param questionMessage the question asked about the subgoal
     * @return this builder
     */
    public SubgoalQuestionAnswerBuilder setQuestionMessage(String questionMessage) {
        this.questionMessage = questionMessage;
        return this;
    }

    /**
     * Sets the response message given for the subgoal question.
     * @param responseMessage the generated or provided answer message
     * @return this builder
     */
    public SubgoalQuestionAnswerBuilder setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
        return this;
    }

    /**
     * Builds and returns a SubgoalQuestionAnswer instance
     * using the configured fields.
     * @return a new SubgoalQuestionAnswer object
     */
    public SubgoalQuestionAnswer build() {
        return new SubgoalQuestionAnswer(id, subgoalId, questionMessage, responseMessage);
    }
}
