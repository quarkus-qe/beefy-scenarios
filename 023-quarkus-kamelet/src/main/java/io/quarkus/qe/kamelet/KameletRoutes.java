package io.quarkus.qe.kamelet;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class KameletRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        routeTemplate("setBody")
                .templateParameter("bodyValue")
                .from("kamelet:source")
                .setBody().constant("Hello {{bodyValue}}");

        routeTemplate("tick")
                .from("timer:{{routeId}}?repeatCount=1&delay=-1")
                .setBody().exchangeProperty(Exchange.TIMER_COUNTER)
                .to("kamelet:sink");

        routeTemplate("setBodyFromProperties")
                .templateParameter("bodyValueFromProperty")
                .from("kamelet:source")
                .setBody().constant("Hello {{bodyValueFromProperty}}");

        routeTemplate("echo")
                .templateParameter("prefix")
                .templateParameter("suffix")
                .from("kamelet:source")
                .setBody().simple("{{prefix}} ${body} {{suffix}}");

        from("direct:chain")
                .to("kamelet:echo/1?prefix=Camel Quarkus&suffix=Chained")
                .to("kamelet:echo/2?prefix=Hello&suffix=Route");
    }
}
