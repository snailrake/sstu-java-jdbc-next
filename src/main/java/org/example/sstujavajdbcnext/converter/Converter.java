package org.example.sstujavajdbcnext.converter;

public abstract class Converter<T> {

    public T convert(String input) {
        try {
            return doConvert(input);
        } catch (Exception e) {
            return null;
        }
    }

    protected abstract T doConvert(String input);

    public abstract Class<T> getClazz();
}
