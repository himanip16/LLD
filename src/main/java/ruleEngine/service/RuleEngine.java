package ruleEngine.service;

import ruleEngine.core.Rule;
import ruleEngine.models.Facts;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    private final List<Rule> registeredRules = new ArrayList<>();

    public void registerRule(Rule rule) {
        this.registeredRules.add(rule);
    }

    public void evaluateAll(Facts facts) {
        for (Rule rule : registeredRules) {
            if (rule.evaluate(facts)) {
                rule.execute(facts);
            }
        }
    }
}