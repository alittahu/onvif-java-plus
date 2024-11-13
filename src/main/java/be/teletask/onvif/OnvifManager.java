//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package be.teletask.onvif;

import be.teletask.onvif.listeners.OnvifDeviceInformationListener;
import be.teletask.onvif.listeners.OnvifMediaStreamURIListener;
import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.listeners.OnvifServicesListener;
import be.teletask.onvif.models.OnvifCreatePullPointSubscription;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifMediaProfile;
import be.teletask.onvif.requests.*;
import be.teletask.onvif.responses.OnvifResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

// Modified by Boj on 20241103
// This file includes modifications based on the original project ONVIF-java.
public class OnvifManager implements OnvifResponseListener {
    public static final String TAG = OnvifManager.class.getSimpleName();
    private OnvifExecutor executor;
    private OnvifResponseListener onvifResponseListener;

    public OnvifManager() {
        this((OnvifResponseListener) null);
    }

    private OnvifManager(OnvifResponseListener onvifResponseListener) {
        this.onvifResponseListener = onvifResponseListener;
        this.executor = new OnvifExecutor(this);
    }

    public void getServices(OnvifDevice device, OnvifServicesListener listener) {
        OnvifRequest request = new GetServicesRequest(listener);
        this.executor.sendRequest(device, request);
    }

    public void getDeviceInformation(OnvifDevice device, OnvifDeviceInformationListener listener) {
        OnvifRequest request = new GetDeviceInformationRequest(listener);
        this.executor.sendRequest(device, request);
    }

    public List<OnvifMediaProfile> getMediaProfiles(OnvifDevice device) throws Exception {
        return this.executor.sendProfileRequest(device, new GetMediaProfilesRequest());
    }

    public void getMediaStreamURI(OnvifDevice device, OnvifMediaProfile profile, OnvifMediaStreamURIListener listener) {
        OnvifRequest request = new GetMediaStreamRequest(profile, listener);
        this.executor.sendRequest(device, request);
    }


    public void sendOnvifRequest(OnvifDevice device, OnvifRequest request) {
        this.executor.sendRequest(device, request);
    }

    /**
     * 云台持续运动
     *
     * @param device       onvif设备
     * @param profileToken mediaProfileToken
     * @param panSpeed     x坐标移动速度
     * @param tiltSpeed    Y坐标移动速度
     * @param zoomSpeed    镜头拉近速度
     */
    public void sendMoveRequestAndBody(OnvifDevice device, String profileToken, double panSpeed, double tiltSpeed, double zoomSpeed) {
        this.executor.sendMoveRequestAndBody(device, GetPTZContinuousMoveRequest.getInstance(), profileToken, panSpeed, tiltSpeed, zoomSpeed);
    }

    /**
     * 云台停止
     *
     * @param device       onvif设备
     * @param profileToken mediaProfileToken
     * @param panTilt      是否立即停止xy坐标的运动
     * @param zoom         是否立即停止镜头运动
     */
    public void sendStopRequest(OnvifDevice device, String profileToken, Boolean panTilt, Boolean zoom) {
        this.executor.sendStopRequest(device, GetPTZStopRequest.getInstance(), profileToken, panTilt, zoom);
    }

    /**
     * 发送创建拉取点订阅
     * @param device onvif设备
     * @param request 请求
     * @param filterExpression 指定订阅的topic
     * @return 拉取点类包含拉取点的相关对象
     * @throws Exception 异常抛出给调用方
     */
    public OnvifCreatePullPointSubscription sendCreatePullPointSubscription(OnvifDevice device, OnvifRequest request, String filterExpression) throws Exception {
        OnvifCreatePullPointSubscription onvifCreatePullPointSubscription = new OnvifCreatePullPointSubscription();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        OnvifResponse onvifResponse = this.executor.sendCreatePullPointSubscription(device, request, filterExpression);

        if (Objects.nonNull(onvifResponse) && onvifResponse.getErrorCode() == 0) {
            Document document = builder.parse(new java.io.ByteArrayInputStream(onvifResponse.getXml().getBytes(StandardCharsets.UTF_8)));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
            // 使用命名空间来获取 Address 元素
            NodeList nodeList = document.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing", "Address");
            if (nodeList.getLength() > 0) {
                Element addressElement = (Element) nodeList.item(0);
                String address = addressElement.getTextContent();
                onvifCreatePullPointSubscription.setSubscriptionReference(address);
            }
            nodeList = document.getElementsByTagNameNS("http://docs.oasis-open.org/wsn/b-2", "CurrentTime");
            if (nodeList.getLength() > 0) {
                Element currentTimeElement = (Element) nodeList.item(0);
                String currentTime = currentTimeElement.getTextContent();
                onvifCreatePullPointSubscription.setCurrentTime(LocalDateTime.parse(currentTime, dateTimeFormatter));
            }
            nodeList = document.getElementsByTagNameNS("http://docs.oasis-open.org/wsn/b-2", "TerminationTime");
            if (nodeList.getLength() > 0) {
                Element terminationTimeElement = (Element) nodeList.item(0);
                String terminationTime = terminationTimeElement.getTextContent();
                onvifCreatePullPointSubscription.setTerminationTime(LocalDateTime.parse(terminationTime, dateTimeFormatter));
            }
        }
        return onvifCreatePullPointSubscription;
    }

    /**
     * 发送拉取消息的请求
     * @param device onvif设备类
     * @param subscriptionPolicyUrl 订阅点的url
     * @param timeout 指定时间内的消息
     * @param messageLimit 拉取消息得到条数
     */
    public void sendPullMessageRequest(OnvifDevice device, String subscriptionPolicyUrl, String timeout, int messageLimit) {
        this.executor.sendPullMessageRequest(device, GetEventsPullMessageRequest.getInstance(), subscriptionPolicyUrl, timeout, messageLimit);
    }

    /**
     * 取消消息的订阅
     * @param device onvif设备
     * @param subscriptionPolicyUrl 订阅点的url
     * @return onvif响应
     */
    public OnvifResponse sendUnsubscribeRequest(OnvifDevice device, String subscriptionPolicyUrl) {
        return this.executor.sendUnsubscribeRequest(device, GetEventsUnsubscribeRequest.getInstance(), subscriptionPolicyUrl);
    }

    public void setOnvifResponseListener(OnvifResponseListener onvifResponseListener) {
        this.onvifResponseListener = onvifResponseListener;
    }

    public void destroy() {
        this.onvifResponseListener = null;
        this.executor.clear();
    }

    public void onResponse(OnvifDevice onvifDevice, OnvifResponse response) {
        if (this.onvifResponseListener != null) {
            this.onvifResponseListener.onResponse(onvifDevice, response);
        }

    }

    public void onError(OnvifDevice onvifDevice, int errorCode, String errorMessage) {
        if (this.onvifResponseListener != null) {
            this.onvifResponseListener.onError(onvifDevice, errorCode, errorMessage);
        }

    }
}
