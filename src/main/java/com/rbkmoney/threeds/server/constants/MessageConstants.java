package com.rbkmoney.threeds.server.constants;

public class MessageConstants {

    public static final String CARDHOLDER_ACCOUNT_NUMBER_IS_NOT_IN_A_RANGE_BELONGING_TO_ISSUER =
            "Cardholder Account Number is not in a range belonging to Issuer";
    public static final String A_MESSAGE_ELEMENT_REQUIRED_AS_DEFINED_IN_TABLE_A_1_IS_MISSING_FROM_THE_MESSAGE =
            "A message element required as defined in Table A.1 is missing from the message";
    public static final String CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED = "Critical message extension not recognised";
    public static final String DATA_ELEMENT_NOT_IN_THE_REQUIRED_FORMAT_OR_VALUE_IS_INVALID_AS_DEFINED_IN_TABLE_A_1 =
            "Data element not in the required format or value is invalid as defined in Table A.1";
    public static final String TRANSACTION_ID_RECEIVED_IS_NOT_VALID_FOR_THE_RECEIVING_COMPONENT =
            "Transaction ID received is not valid for the receiving component";
    public static final String TRANSACTION_TIMED_OUT = "Transaction timed-out";
    public static final String SYSTEM_CONNECTION_FAILURE = "System connection failure";
    public static final String MESSAGE_NOT_RECOGNISED = "Message not recognised";
    public static final String THE_SENDING_COMPONENT_IS_UNABLE_TO_ESTABLISH_CONNECTION_TO_THE_RECEIVING_COMPONENT =
            "The sending component is unable to establish connection to the receiving component";
    public static final String INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT =
            "Invalid Message for the receiving component";
    public static final String MESSAGE_TYPE = "messageType";
    public static final String TIMEOUT_EXPIRY_REACHED_FOR_THE_TRANSACTION_AS_DEFINED_IN_SECTION_5_5 =
            "Timeout expiry reached for the transaction as defined in Section 5.5";
    public static final String UNSUPPORTED_MESSAGE_TYPE =
            "Valid Message Type is sent to or from an inappropriate component " +
                    "(such as AReq message being sent to the 3DS Server)";

}
