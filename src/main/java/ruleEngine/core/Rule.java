package ruleEngine.core;

import ruleEngine.models.Facts;


import ruleEngine.models.Facts;

public class Rule {
    private final String name;
    private final Expression rootExpression;
    private final Runnable action;

    public Rule(String name, Expression rootExpression, Runnable action) {
        this.name = name;
        this.rootExpression = rootExpression;
        this.action = action;
    }

    public boolean evaluate(Facts facts) {
        return rootExpression.interpret(facts);
    }

    public void execute() {
        action.run();
    }

    public String getName() { return name; }
}