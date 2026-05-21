package ruleEngine.expressions;

import ruleEngine.core.Expression;
import ruleEngine.models.Facts;

public class GreaterThanExpression implements Expression {
    private final String key;
    private final double threshold;

    public GreaterThanExpression(String key, double threshold) {
        this.key = key;
        this.threshold = threshold;
    }

    @Override
    public boolean interpret(Facts facts) {
        if (!facts.has(key)) return false;
        Object val = facts.get(key);
        if (val instanceof Number) {
            return ((Number) val).doubleValue() > threshold;
        }
        return false;
    }
}
