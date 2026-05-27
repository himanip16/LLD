package ruleEngine.composites;

import ruleEngine.core.Expression;
import ruleEngine.core.Rule;
import ruleEngine.models.Facts;
import java.util.ArrayList;
import java.util.List;

public class AndCompositeRule extends Rule {
    private final List<Rule> childRules = new ArrayList<>();

    public AndCompositeRule(String name, Expression rootExpression, Runnable action) {
        super(name, rootExpression, action);
    }

    public AndCompositeRule add(Rule rule) {
        childRules.add(rule);
        return this;
    }

    @Override
    public boolean evaluate(Facts facts) {
        if (childRules.isEmpty()) return false;

        // All child conditions must pass
        for (Rule rule : childRules) {
            if (!rule.evaluate(facts)) return false;
        }
        return true;
    }

    @Override
    public void execute(Facts facts) {
        // Trigger actions down the branch chain sequentially
        childRules.forEach(rule -> rule.execute(facts));
    }
}