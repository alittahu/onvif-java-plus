//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package be.teletask.onvif;

import be.teletask.onvif.listeners.OnvifDeviceInformationListener;
import be.teletask.onvif.listeners.OnvifMediaProfilesListener;
import be.teletask.onvif.listeners.OnvifMediaStreamURIListener;
import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.listeners.OnvifServicesListener;
import be.teletask.onvif.models.OnvifCreatePullPointSubscription;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifMediaProfile;
import be.teletask.onvif.requests.GetDeviceInformationRequest;
import be.teletask.onvif.requests.GetMediaProfilesRequest;
import be.teletask.onvif.requests.GetMediaStreamRequest;
import be.teletask.onvif.requests.GetServicesRequest;
import be.teletask.onvif.requests.OnvifRequest;
import be.teletask.onvif.responses.OnvifResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    public void getMediaProfiles(OnvifDevice device, OnvifMediaProfilesListener listener) {
        OnvifRequest request = new GetMediaProfilesRequest(listener);
        this.executor.sendRequest(device, request);
    }

    public void getMediaStreamURI(OnvifDevice device, OnvifMediaProfile profile, OnvifMediaStreamURIListener listener) {
        OnvifRequest request = new GetMediaStreamRequest(profile, listener);
        this.executor.sendRequest(device, request);
    }

    public void sendOnvifRequest(OnvifDevice device, OnvifRequest request) {
        this.executor.sendRequest(device, request);
    }

    public void sendMoveRequestAndBody(OnvifDevice device, OnvifRequest request, String profileToken, double panSpeed, double tiltSpeed, double zoomSpeed) {
        this.executor.sendMoveRequestAndBody(device, request, profileToken, panSpeed, tiltSpeed, zoomSpeed);
    }

    public void sendStopRequest(OnvifDevice device, OnvifRequest request, String profileToken, Boolean panTilt, Boolean zoom) {
        this.executor.sendStopRequest(device, request, profileToken, panTilt, zoom);
    }

    public OnvifCreatePullPointSubscription sendCreatePullPointSubscription(OnvifDevice device, OnvifRequest request, String filterExpression) throws Exception {
        OnvifCreatePullPointSubscription onvifCreatePullPointSubscription = new OnvifCreatePullPointSubscription();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // 使其支持命名空间
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

    public void sendPullMessageRequest(OnvifDevice device, OnvifRequest request, String subscriptionPolicyUrl, String timeout, int messageLimit) {
        this.executor.sendPullMessageRequest(device, request, subscriptionPolicyUrl, timeout, messageLimit);
    }

    public OnvifResponse sendUnsubscribeRequest(OnvifDevice device, OnvifRequest request, String subscriptionPolicyUrl) {
        return this.executor.sendUnsubscribeRequest(device, request, subscriptionPolicyUrl);
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
