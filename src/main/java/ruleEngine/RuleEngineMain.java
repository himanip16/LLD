package ruleEngine;

import ruleEngine.core.Expression;
import ruleEngine.core.Rule;
import ruleEngine.expressions.AndExpression;
import ruleEngine.expressions.EqualsExpression;
import ruleEngine.expressions.GreaterThanExpression;
import ruleEngine.expressions.OrExpression;
import ruleEngine.models.Facts;
import ruleEngine.service.RuleEngine;

public class RuleEngineMain {

    public static void main(String[] args) {
        RuleEngine engine = new RuleEngine();

        // 1. Build the Hierarchical Expression Tree:
        // (Age > 25 AND Income > 50000) OR IsVIP == true
        Expression ageCondition = new GreaterThanExpression("age", 25);
        Expression incomeCondition = new GreaterThanExpression("income", 50000);

        Expression compoundAnd = new AndExpression(ageCondition, incomeCondition);
        Expression vipCondition = new EqualsExpression("isVIP", true);

        Expression finalRootExpression = new OrExpression(compoundAnd, vipCondition);

        // 2. Wrap it inside a functional, reusable Rule object
        Rule premiumDiscountRule = new Rule(
                "Premium Holiday Discount Promotion",
                finalRootExpression,
                () -> System.out.println("🎉 Applied a flat 20% discount package to account checkout context!")
        );

        engine.registerRule(premiumDiscountRule);

        // --- SCENARIO A: Meets the AND criteria (Age > 25 and Income > 50000), not a VIP
        System.out.println("=== Testing Customer Profile A ===");
        Facts customerA = new Facts();
        customerA.put("age", 30);
        customerA.put("income", 75000);
        customerA.put("isVIP", false);
        engine.evaluateAndExecute(customerA);

        // --- SCENARIO B: Fails the AND criteria (Low income) but passes via VIP fallback
        System.out.println("\n=== Testing Customer Profile B ===");
        Facts customerB = new Facts();
        customerB.put("age", 21);
        customerB.put("income", 12000);
        customerB.put("isVIP", true);
        engine.evaluateAndExecute(customerB);

        // --- SCENARIO C: Fails both paths entirely
        System.out.println("\n=== Testing Customer Profile C ===");
        Facts customerC = new Facts();
        customerC.put("age", 19);
        customerC.put("income", 20000);
        customerC.put("isVIP", false);
        engine.evaluateAndExecute(customerC); // Will print nothing
    }
}
