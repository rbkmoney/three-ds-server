package com.rbkmoney.threeds.server.utils;

import lombok.RequiredArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class TemplateBuilder {

    private final VelocityEngine templateEngine;

    public String buildTemplate(String templatePath, Consumer<VelocityContext> velocityContextConsumer) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContextConsumer.accept(velocityContext);

        StringWriter writer = new StringWriter();

        Template template = templateEngine.getTemplate(templatePath);
        template.merge(velocityContext, writer);
        return writer.toString();
    }
}
