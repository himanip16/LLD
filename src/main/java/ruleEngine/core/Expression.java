package ruleEngine.core;

import ruleEngine.models.Facts;

public interface Expression {
    boolean interpret(Facts facts);
}