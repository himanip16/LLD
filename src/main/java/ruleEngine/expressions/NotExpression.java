package ruleEngine.expressions;

import ruleEngine.core.Expression;
import ruleEngine.models.Facts;

public class NotExpression implements Expression {
    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean interpret(Facts facts) {
        return !expression.interpret(facts);
    }
}
