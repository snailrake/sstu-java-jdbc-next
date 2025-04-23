package org.example.sstujavajdbcnext.operator;

import org.springframework.stereotype.Component;

@Component
public class EqualsOperator extends Operator {

    @Override
    public String getOperatorType() {
        return "Equals";
    }

    @Override
    protected String buildCondition(String field) {
        return field + " = ?";
    }
}
