package ruleEngine.rules;

import ruleEngine.core.Rule;
import ruleEngine.models.Facts;

public class RestrictiveCountryRule implements Rule {
    @Override
    public boolean evaluate(Facts facts) {
        String country = facts.get("country");
        return "RESTRICTED_REGION".equalsIgnoreCase(country);
    }

    @Override
    public void execute(Facts facts) {
        System.out.println("🚫 Action: Injecting geoblock restrictions into streaming context.");
    }
}
