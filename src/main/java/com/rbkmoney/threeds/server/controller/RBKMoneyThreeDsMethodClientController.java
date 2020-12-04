package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDSMethod;
import com.rbkmoney.threeds.server.dto.ThreeDsMethodFormData;
import com.rbkmoney.threeds.server.utils.Base64Encoder;
import com.rbkmoney.threeds.server.utils.TemplateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
@RequiredArgsConstructor
@Slf4j
public class RBKMoneyThreeDsMethodClientController {

    private static final String TITLE = "threeDsMethodFormData";
    private static final String TEMPLATE_PATH = "vm/ThreeDsMethodForm.vm";

    private final Base64Encoder base64Encoder;
    private final TemplateBuilder templateBuilder;

    @PostMapping("/three-ds-method")
    public ResponseEntity<String> threeDSMethodHandler(@RequestBody ThreeDSMethod threeDSMethod) {
        log.info("Build template with ThreeDSMethodData, threeDSMethod={}", threeDSMethod.toString());

        String form = templateBuilder.buildTemplate(
                TEMPLATE_PATH,
                velocityContext -> velocityContext.put(TITLE, threeDsMethodFormData(threeDSMethod)));

        return ResponseEntity.ok(form);
    }

    private ThreeDsMethodFormData threeDsMethodFormData(@RequestBody ThreeDSMethod threeDSMethod) {
        return ThreeDsMethodFormData.builder()
                .threeDSMethodData(base64Encoder.encode(threeDSMethod.getThreeDSMethodData()))
                .threeDSMethodURL(threeDSMethod.getThreeDSMethodURL())
                .build();
    }
}
