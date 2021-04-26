package quarkus.qe.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class CustomEmailConverter implements Converter<KrustyEmail> {

    @Override
    public KrustyEmail convert(String str) {
        String email = str.replaceAll("\\s","").toLowerCase()+"@krustykrab.com";
        return new KrustyEmail(email);
    }
}