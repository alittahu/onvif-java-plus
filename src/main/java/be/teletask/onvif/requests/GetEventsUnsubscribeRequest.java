package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;


public class GetEventsUnsubscribeRequest implements OnvifRequest {

    public static final String TAG = GetEventsUnsubscribeRequest.class.getSimpleName();

    private static GetEventsUnsubscribeRequest instance;

    private GetEventsUnsubscribeRequest() {}

    public static synchronized GetEventsUnsubscribeRequest getInstance() {
        if (instance == null) {
            instance = new GetEventsUnsubscribeRequest();
        }
        return instance;
    }
    @Override
    public String getXml() {
        return "<Unsubscribe xmlns=\"http://docs.oasis-open.org/wsn/bw-2/SubscriptionManager/UnsubscribeRequest\"></Unsubscribe>";
    }
 
    @Override
    public OnvifType getType() {
        // 协议分类
        return OnvifType.GET_EVENTS_URI;
    }
}