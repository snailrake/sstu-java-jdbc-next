package org.example.sstujavajdbcnext.converter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ConverterContext {

    private List<Converter<?>> converters;

    private Map<Class<?>, Converter<?>> convertersMap;

    public ConverterContext(List<Converter<?>> converters) {
        this.converters = converters;
    }

    public Converter<?> getConverter(Class<?> clazz) {
        if (convertersMap == null) {
            convertersMap = converters.stream()
                    .collect(Collectors.toMap(
                            Converter::getClazz,
                            Function.identity())
                    );
        }
        return convertersMap.get(clazz);
    }
}
