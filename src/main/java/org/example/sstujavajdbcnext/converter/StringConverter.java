package org.example.sstujavajdbcnext.converter;

import org.springframework.stereotype.Component;

@Component
public class StringConverter extends Converter<String> {

    @Override
    protected String doConvert(String input) {
        return input;
    }

    @Override
    public Class<String> getClazz() {
        return String.class;
    }
}
