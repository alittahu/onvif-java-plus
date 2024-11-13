package be.teletask.onvif;

import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.OnvifCreatePullPointSubscription;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifType;
import be.teletask.onvif.requests.GetEventsCreatePullPointSubscriptionRequest;
import be.teletask.onvif.requests.GetEventsPullMessageRequest;
import be.teletask.onvif.requests.GetEventsUnsubscribeRequest;
import be.teletask.onvif.responses.OnvifResponse;
import be.teletask.onvif.util.OnvifResponsesAnalyzeUtils;
import org.junit.Test;

import java.util.Map;
import java.util.Objects;

/**
 * @author Boj
 * @desc Onvif订阅事件的单元测试类
 * @since 11/8/2024 2:45 PM
 */
public class EventTest {

    private final static String USER_NAME = "admin";
    private final static String PASSWORD = "a1234567";

    private final static String IP = "192.168.0.136";

    public static OnvifManager ONVIF_MANGER = new OnvifManager();

    public static OnvifDevice ONVIF_DEVICE = null;

    static {
        ONVIF_DEVICE = new be.teletask.onvif.models.OnvifDevice(IP, USER_NAME, PASSWORD);

        ONVIF_MANGER.setOnvifResponseListener(new OnvifResponseListener() {
            // 请求成功处理
            @Override
            public void onResponse(be.teletask.onvif.models.OnvifDevice onvifDevice, OnvifResponse onvifResponse) {
                System.out.println("onvifResponse = " + onvifResponse.getXml());
                OnvifType requestType = onvifResponse.getRequestType();
                switch (requestType) {
                    case GET_EVENTS_PULL_MESSAGE_URI:
                        // 此处获取了本次事件的所有data数据标签下的所有值
                        Map<String, String> pullMessageValues = OnvifResponsesAnalyzeUtils.getPullMessageValues(onvifResponse.getXml());

                        String isMotion = pullMessageValues.get("IsMotion");
                        if (Boolean.parseBoolean(isMotion)) {
                            System.out.println("\n有人经过");
                        } else {
                            System.out.println("\n无人经过");
                        }
                        break;
                    case GET_EVENTS_URI:
                        System.out.println("onvifResponse = " + onvifResponse.getXml());
                        break;
                    default:
                        System.out.println("onvifResponse = " + onvifResponse.getXml());
                        break;
                }
            }

            //请求失败处理
            @Override
            public void onError(be.teletask.onvif.models.OnvifDevice onvifDevice, int i, String s) {
                System.out.println("error = " + s);
                System.out.println(("失败"));
            }
        });
    }

    /**
     * 获取拉取点并重复调用获取事件信息
     * @throws Exception 抛出异常
     */
    @Test
    public void pullMessage() throws Exception {
        // 发起订阅拿到拉取对象，并指定订阅的事件topic
        OnvifCreatePullPointSubscription onvifCreatePullPointSubscription = ONVIF_MANGER.sendCreatePullPointSubscription
                (ONVIF_DEVICE, GetEventsCreatePullPointSubscriptionRequest.getInstance(), "tns1:RuleEngine/CellMotionDetector/Motion");
        if (Objects.nonNull(onvifCreatePullPointSubscription)) {
            String subscriptionReference = onvifCreatePullPointSubscription.getSubscriptionReference();
            while (true) {
                // 发送拉取api拉取10秒内的10条信息
                ONVIF_MANGER.sendPullMessageRequest(ONVIF_DEVICE, subscriptionReference, "PT100S", 10);
            }
        }
    }

    /**
     * 取消订阅拉取点并立即销毁拉取点
     */
    @Test
    public void unsubscribe(){
        OnvifResponse onvifResponse = ONVIF_MANGER.sendUnsubscribeRequest(ONVIF_DEVICE, "http://192.168.0.136/onvif/Events/PullSubManager_20241108T074250Z_2");
        System.out.println("onvifResponse.getXml() = " + onvifResponse.getXml());
    }

}
