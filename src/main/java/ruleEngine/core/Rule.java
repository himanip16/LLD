package ruleEngine.core;

import ruleEngine.models.Facts;

public interface Rule {
    boolean evaluate(Facts facts);
    void execute(Facts facts);
}