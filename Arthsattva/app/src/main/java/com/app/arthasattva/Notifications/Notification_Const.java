package com.app.arthasattva.Notifications;

public interface Notification_Const {
    String serverKey = "AAAAJOnQM_c:APA91bEiUlVT04u_xlVkpRJl4VeXzZ7NX-cl9AIRaRMfEfeLcwVouX1t9xSz-VDw7u3qNErFf_kZNa4r8sk4hGe48REgoBhJ7tR0vcYhkoI4huy13qZ9HOeI2R-vF4WIeirn0eBBNMMG";

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
