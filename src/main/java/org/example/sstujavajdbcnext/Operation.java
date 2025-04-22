package org.example.sstujavajdbcnext;

public class Operation {

    private String operator;

    private String field;

    public Operation(String operator, String field) {
        this.operator = operator;
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public String getField() {
        return field;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setField(String field) {
        this.field = field;
    }
}
