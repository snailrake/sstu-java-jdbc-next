package org.example.sstujavajdbcnext.converter;

import org.springframework.stereotype.Component;

@Component
public class IntegerConverter extends Converter<Integer> {

    @Override
    protected Integer doConvert(String input) {
        return Integer.parseInt(input);
    }

    @Override
    public Class<Integer> getClazz() {
        return Integer.class;
    }
}
