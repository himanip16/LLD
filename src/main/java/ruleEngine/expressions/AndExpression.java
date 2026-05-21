package ruleEngine.expressions;

import ruleEngine.core.Expression;
import ruleEngine.models.Facts;

public class AndExpression implements Expression {
    private final Expression left;
    private final Expression right;

    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(Facts facts) {
        return left.interpret(facts) && right.interpret(facts);
    }
}