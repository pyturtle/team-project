package entity.subgoal;
import java.util.UUID;

public class SubgoalQuestionAnswerBuilder {
    private String id= "";
    private String subgoalId = "";
    private String questionMessage = "";
    private String responseMessage = "";

    public SubgoalQuestionAnswerBuilder generateId() {
        this.id = UUID.randomUUID().toString();
        return this;
    }

    public SubgoalQuestionAnswerBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public SubgoalQuestionAnswerBuilder setSubgoalId(String subgoalId) {
        this.subgoalId = subgoalId;
        return this;
    }

    public SubgoalQuestionAnswerBuilder setQuestionMessage(String questionMessage) {
        this.questionMessage = questionMessage;
        return this;
    }

    public SubgoalQuestionAnswerBuilder setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
        return this;
    }

    public SubgoalQuestionAnswer build() {
        return new SubgoalQuestionAnswer(id, subgoalId, questionMessage, responseMessage);
    }
}
