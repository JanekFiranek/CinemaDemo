package tech.firanek.cinemademo.misc;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;

public class StringToInstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(@NonNull String source) {
        return Instant.ofEpochSecond(Long.parseLong(source));
    }
}
