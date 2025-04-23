package org.example.sstujavajdbcnext.operator;

import java.util.List;

public abstract class Operator {

    public void apply(String field, Object value, StringBuilder whereCondition, List<Object> whereParams) {
        if (!whereCondition.isEmpty()) {
            whereCondition.append(" AND ");
        }
        whereCondition.append(buildCondition(field));
        whereParams.add(value);
    }

    public abstract String getOperatorType();

    protected abstract String buildCondition(String field);
}
