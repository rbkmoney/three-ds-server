package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.dto.ConstraintType;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.constants.MessageConstants.A_MESSAGE_ELEMENT_REQUIRED_AS_DEFINED_IN_TABLE_A_1_IS_MISSING_FROM_THE_MESSAGE;
import static com.rbkmoney.threeds.server.constants.MessageConstants.CARDHOLDER_ACCOUNT_NUMBER_IS_NOT_IN_A_RANGE_BELONGING_TO_ISSUER;
import static com.rbkmoney.threeds.server.constants.MessageConstants.CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED;
import static com.rbkmoney.threeds.server.constants.MessageConstants.DATA_ELEMENT_NOT_IN_THE_REQUIRED_FORMAT_OR_VALUE_IS_INVALID_AS_DEFINED_IN_TABLE_A_1;
import static com.rbkmoney.threeds.server.constants.MessageConstants.INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT;
import static com.rbkmoney.threeds.server.constants.MessageConstants.MESSAGE_NOT_RECOGNISED;
import static com.rbkmoney.threeds.server.constants.MessageConstants.MESSAGE_TYPE;
import static com.rbkmoney.threeds.server.constants.MessageConstants.SYSTEM_CONNECTION_FAILURE;
import static com.rbkmoney.threeds.server.constants.MessageConstants.THE_SENDING_COMPONENT_IS_UNABLE_TO_ESTABLISH_CONNECTION_TO_THE_RECEIVING_COMPONENT;
import static com.rbkmoney.threeds.server.constants.MessageConstants.TIMEOUT_EXPIRY_REACHED_FOR_THE_TRANSACTION_AS_DEFINED_IN_SECTION_5_5;
import static com.rbkmoney.threeds.server.constants.MessageConstants.TRANSACTION_ID_RECEIVED_IS_NOT_VALID_FOR_THE_RECEIVING_COMPONENT;
import static com.rbkmoney.threeds.server.constants.MessageConstants.TRANSACTION_TIMED_OUT;

@Component
public class ErrorMessageResolver {

    public String resolveErrorDescription(String messageTemplate) {
        ConstraintType constraintType = ConstraintType.of(messageTemplate);
        switch (constraintType) {
            case OUT_OF_CARD_RANGE:
                return CARDHOLDER_ACCOUNT_NUMBER_IS_NOT_IN_A_RANGE_BELONGING_TO_ISSUER;
            case NOT_BLANK:
            case NOT_NULL:
                return A_MESSAGE_ELEMENT_REQUIRED_AS_DEFINED_IN_TABLE_A_1_IS_MISSING_FROM_THE_MESSAGE;
            case CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED:
                return CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED;
            case PATTERN:
                return DATA_ELEMENT_NOT_IN_THE_REQUIRED_FORMAT_OR_VALUE_IS_INVALID_AS_DEFINED_IN_TABLE_A_1;
            case ID_NOT_RECOGNISED:
                return TRANSACTION_ID_RECEIVED_IS_NOT_VALID_FOR_THE_RECEIVING_COMPONENT;
            case AUTH_DEC_TIME_IS_EXPIRED:
                return TRANSACTION_TIMED_OUT;
            default:
                throw new IllegalArgumentException("Unsupported annotation type: " + messageTemplate);
        }
    }

    public String resolveDefaultErrorDescription(ErrorCode errorCode) {
        switch (errorCode) {
            case SYSTEM_CONNECTION_FAILURE_405:
                return SYSTEM_CONNECTION_FAILURE;
            case MESSAGE_RECEIVED_INVALID_101:
                return MESSAGE_NOT_RECOGNISED;
            case FORMAT_OF_DATA_ELEMENTS_INVALID_203:
                return DATA_ELEMENT_NOT_IN_THE_REQUIRED_FORMAT_OR_VALUE_IS_INVALID_AS_DEFINED_IN_TABLE_A_1;
            case REQUIRED_DATA_ELEMENT_MISSING_201:
                return A_MESSAGE_ELEMENT_REQUIRED_AS_DEFINED_IN_TABLE_A_1_IS_MISSING_FROM_THE_MESSAGE;
            case TRANSACTION_TIMED_OUT_402:
                return TRANSACTION_TIMED_OUT;
            default:
                return null;
        }
    }

    public String resolveDefaultErrorDetail(ErrorCode errorCode) {
        switch (errorCode) {
            case SYSTEM_CONNECTION_FAILURE_405:
                return THE_SENDING_COMPONENT_IS_UNABLE_TO_ESTABLISH_CONNECTION_TO_THE_RECEIVING_COMPONENT;
            case MESSAGE_RECEIVED_INVALID_101:
                return INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT;
            case FORMAT_OF_DATA_ELEMENTS_INVALID_203:
            case REQUIRED_DATA_ELEMENT_MISSING_201:
                return MESSAGE_TYPE;
            case TRANSACTION_TIMED_OUT_402:
                return TIMEOUT_EXPIRY_REACHED_FOR_THE_TRANSACTION_AS_DEFINED_IN_SECTION_5_5;
            default:
                return null;
        }
    }
}
