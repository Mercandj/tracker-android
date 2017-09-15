package com.mercandalli.tracker.push;

public interface PushSenderManager {

    void sendPush(String cloudMessagingId, String message);
}
