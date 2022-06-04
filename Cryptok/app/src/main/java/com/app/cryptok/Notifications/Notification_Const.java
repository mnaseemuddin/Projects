package com.app.cryptok.Notifications;

public interface Notification_Const {
    String serverKey = "AAAAMjGvFCc:APA91bHonSNhJNarUNSuG-aWcf7v4kn0E2w4PnqVZ-rdPS38ipLPne4up3K8QFN4kyvFuvsNBRssVZ3YEppOU3Ktf5lt5jaHpS986X9LvUACHnd0wtPLPKTL080hdTTUo9xYc37-HJMM";

    String title = "title";
    String message = "message";
    String user_id = "user_id";
    String super_live_id = "super_live_id";
    String context_message = "context_message";
    String notification_type = "notification_type";
    String notification_data = "notification_data";
    String alert_type = "alert_type";
    int token_type = 1;
    int topic_type = 2;
    String body = "body";
    String NORMAL_NOTIFICATION_TYPE = "NORMAL_NOTIFICATION_TYPE";
    String IMAGE_NOTIFICATION_TYPE = "IMAGE_NOTIFICATION_TYPE";
    String INVITE_STREAM_NOTIFICATION = "INVITE_STREAM_NOTIFICATION";
    String topic_base_url="/topics/";
    String global_notification_name="global_notification_topic";
    String global_notification_topic = topic_base_url+global_notification_name;

}
