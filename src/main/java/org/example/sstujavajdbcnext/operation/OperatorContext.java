package org.example.sstujavajdbcnext.operation;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OperatorContext {

    private final List<Operator> operators;

    private Map<String, Operator> operatorsMap;

    public OperatorContext(List<Operator> operators) {
        this.operators = operators;
    }

    public Operator getOperator(String operatorName) {
        if (operatorsMap == null) {
            operatorsMap = operators.stream()
                    .collect(Collectors.toMap(
                            Operator::getOperatorType,
                            Function.identity())
                    );
        }
        return operatorsMap.get(operatorName);
    }
}
