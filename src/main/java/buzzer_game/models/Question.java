package buzzer_game.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Question {
    private String questionId;
    private String text;
    private List<Option> options;
    private List<Response> responses;

    public Question(String questionId, String text, List<Option> options) {
        this.questionId = questionId;
        this.text = text;
        this.options = options;
        this.responses = new ArrayList<>();
    }
}