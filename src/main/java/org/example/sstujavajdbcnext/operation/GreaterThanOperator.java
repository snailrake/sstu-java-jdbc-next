package org.example.sstujavajdbcnext.operation;

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
