package com.rbkmoney.threeds.server.controller.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodRequest;
import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodResponse;
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
    public ResponseEntity<ThreeDsMethodResponse> threeDsMethodHandler(@RequestBody ThreeDsMethodRequest request) {
        log.info("Build template with ThreeDsMethodData, threeDsMethodRequest={}", request.toString());

        String htmlThreeDsMethodData = templateBuilder.buildTemplate(
                TEMPLATE_PATH,
                velocityContext -> velocityContext.put(TITLE, threeDsMethodFormData(request)));

        return ResponseEntity.ok(
                ThreeDsMethodResponse.builder()
                        .htmlThreeDsMethodData(htmlThreeDsMethodData)
                        .build());
    }

    private ThreeDsMethodFormData threeDsMethodFormData(ThreeDsMethodRequest request) {
        return ThreeDsMethodFormData.builder()
                .encodeThreeDsMethodData(base64Encoder.encode(request.getThreeDsMethodData()))
                .threeDsMethodUrl(request.getThreeDsMethodUrl())
                .build();
    }
}
