package ruleEngine.service;

import ruleEngine.core.Rule;
import ruleEngine.models.Facts;
import java.util.ArrayList;
import java.util.List;
public class RuleEngine {
    private final List<Rule> businessRules = new ArrayList<>();

    public void registerRule(Rule rule) {
        businessRules.add(rule);
    }

    public void evaluateAndExecute(Facts facts) {
        for (Rule rule : businessRules) {
            if (rule.evaluate(facts)) {
                System.out.print("Rule [" + rule.getName() +"] passed! Triggering -> ");
                rule.execute();
            }
        }
    }
}