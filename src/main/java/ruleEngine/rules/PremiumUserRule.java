package ruleEngine.rules;

import ruleEngine.core.Expression;
import ruleEngine.core.Rule;
import ruleEngine.models.Facts;

public class PremiumUserRule extends Rule {
    public PremiumUserRule(String name, Expression rootExpression, Runnable action) {
        super(name, rootExpression, action);
    }

    @Override
    public boolean evaluate(Facts facts) {
        return facts.has("isPremium") && Boolean.TRUE.equals(facts.get("isPremium"));
    }

    @Override
    public void execute(Facts facts) {
        System.out.println("✨ Action: Applying high-fidelity audio stream configurations.");
    }
}

