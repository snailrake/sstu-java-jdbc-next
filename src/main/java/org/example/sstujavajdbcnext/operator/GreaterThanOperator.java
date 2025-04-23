package org.example.sstujavajdbcnext.operator;

import org.springframework.stereotype.Component;

@Component
public class GreaterThanOperator extends Operator {

    @Override
    public String getOperatorType() {
        return "Greater";
    }

    @Override
    protected String buildCondition(String field) {
        return field + " > ?";
    }
}
