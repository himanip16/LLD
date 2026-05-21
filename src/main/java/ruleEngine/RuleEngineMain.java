package ruleEngine;

import ruleEngine.composites.AndCompositeRule;
import ruleEngine.models.Facts;
import ruleEngine.rules.PremiumUserRule;
import ruleEngine.rules.RestrictiveCountryRule;
import ruleEngine.service.RuleEngine;

public class RuleEngineMain {
    public static void main(String[] args) {
        RuleEngine engine = new RuleEngine();

        // 1. Compose a compound rule pipeline dynamically: (Is Premium AND In Restricted Region)
        AndCompositeRule securityAlertPipeline = new AndCompositeRule()
                .add(new PremiumUserRule())
                .add(new RestrictiveCountryRule());

        engine.registerRule(securityAlertPipeline);

        // 2. Scenario A: User matches all criteria of the composite rule
        System.out.println("=== Running Evaluation Context A ===");
        Facts contextA = new Facts();
        contextA.put("isPremium", true);
        contextA.put("country", "RESTRICTED_REGION");

        engine.evaluateAll(contextA);

        // 3. Scenario B: User only matches one criteria (Rule should safely evaluate to false)
        System.out.println("\n=== Running Evaluation Context B ===");
        Facts contextB = new Facts();
        contextB.put("isPremium", true);
        contextB.put("country", "INDIA");

        engine.evaluateAll(contextB); // Will remain silent because the AND criteria failed
    }
}