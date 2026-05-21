package ruleEngine.rules;

import ruleEngine.core.Rule;
import ruleEngine.models.Facts;

public class PremiumUserRule implements Rule {
    @Override
    public boolean evaluate(Facts facts) {
        return facts.has("isPremium") && Boolean.TRUE.equals(facts.get("isPremium"));
    }

    @Override
    public void execute(Facts facts) {
        System.out.println("✨ Action: Applying high-fidelity audio stream configurations.");
    }
}

