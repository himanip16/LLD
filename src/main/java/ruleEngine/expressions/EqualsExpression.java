package ruleEngine.expressions;

import ruleEngine.core.Expression;
import ruleEngine.models.Facts;

import java.util.Objects;

public class EqualsExpression implements Expression {
    private final String key;
    private final Object value;

    public EqualsExpression(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean interpret(Facts facts) {
        if (!facts.has(key)) return false;
        return Objects.equals(facts.get(key), value);
    }
}
