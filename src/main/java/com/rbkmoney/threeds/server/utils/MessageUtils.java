package com.rbkmoney.threeds.server.utils;

import com.rbkmoney.threeds.server.domain.root.Message;

public class MessageUtils {

    public static Message empty() {
        return new Message() {
            @Override
            public boolean isRelevantMessageVersion() {
                return super.isRelevantMessageVersion();
            }

            @Override
            public String getMessageVersion() {
                return super.getMessageVersion();
            }

            @Override
            public String getUlTestCaseId() {
                return super.getUlTestCaseId();
            }

            @Override
            public Message getRequestMessage() {
                return super.getRequestMessage();
            }

            @Override
            public void setMessageVersion(String messageVersion) {
                super.setMessageVersion(messageVersion);
            }

            @Override
            public void setUlTestCaseId(String ulTestCaseId) {
                super.setUlTestCaseId(ulTestCaseId);
            }

            @Override
            public void setRequestMessage(Message requestMessage) {
                super.setRequestMessage(requestMessage);
            }

            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }

            @Override
            protected boolean canEqual(Object other) {
                return super.canEqual(other);
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public String toString() {
                return super.toString();
            }

            @Override
            public void setHandleRepetitionNeeded(boolean handleRepetitionNeeded) {

            }

            @Override
            public boolean isHandleRepetitionNeeded() {
                return false;
            }

            @Override
            public String getAcsTransID() {
                return null;
            }

            @Override
            public String getDsTransID() {
                return null;
            }

            @Override
            public String getThreeDSServerTransID() {
                return null;
            }

            @Override
            public String getSdkTransID() {
                return null;
            }
        };
    }
}
