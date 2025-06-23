//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package be.teletask.onvif;

import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifMediaProfile;
import be.teletask.onvif.models.OnvifPTZStatus;
import be.teletask.onvif.models.OnvifServices;
import be.teletask.onvif.parsers.*;
import be.teletask.onvif.requests.*;
import be.teletask.onvif.responses.OnvifResponse;
import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

// Modified by alittahu on 20250623
// This file includes modifications based on the original project ONVIF-java.
public class OnvifExecutor {
    public static final String TAG = OnvifExecutor.class.getSimpleName();
    private final OkHttpClient client;
    private final MediaType reqBodyType;
    private RequestBody reqBody;
    private final Credentials credentials;
    private OnvifResponseListener onvifResponseListener;

    OnvifExecutor(OnvifResponseListener onvifResponseListener) {
        this.onvifResponseListener = onvifResponseListener;
        this.credentials = new Credentials("username", "password");
        DigestAuthenticator authenticator = new DigestAuthenticator(this.credentials);
        Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();
        this.client = (new OkHttpClient.Builder()).connectTimeout(10000L, TimeUnit.SECONDS).writeTimeout(100L, TimeUnit.SECONDS).readTimeout(10000L, TimeUnit.SECONDS).authenticator(new CachingAuthenticatorDecorator(authenticator, authCache)).addInterceptor(new AuthenticationCacheInterceptor(authCache)).build();
        this.reqBodyType = MediaType.parse("application/soap+xml; charset=utf-8;");
    }

    void sendRequest(OnvifDevice device, OnvifRequest request) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(OnvifXMLBuilder.getSoapHeader() + request.getXml() + OnvifXMLBuilder.getEnvelopeEnd(), this.reqBodyType);
        this.performXmlRequest(device, request, this.buildOnvifRequest(device, request));
    }

    List<OnvifMediaProfile> sendProfileRequest(OnvifDevice device, OnvifRequest request) throws Exception {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(OnvifXMLBuilder.getSoapHeader() + request.getXml() + OnvifXMLBuilder.getEnvelopeEnd(), this.reqBodyType);
        return this.performXmlProfileRequest(device, request, this.buildOnvifRequest(device, request));
    }

    private List<OnvifMediaProfile> performXmlProfileRequest(OnvifDevice device, OnvifRequest request, Request xmlRequest) throws Exception {
        OnvifResponse onvifResponse = new OnvifResponse(request);
        if (xmlRequest != null) {
            try (Response response = this.client.newCall(xmlRequest).execute()) {
                if (response.isSuccessful()) {
                    onvifResponse.setSuccess(true);

                    ResponseBody body = response.body();
                    if (body != null) {
                        // 将响应的 XML 内容设置到 OnvifResponse 中
                        onvifResponse.setXml(body.string());
                    } else {
                        onvifResponse.setErrorCode(response.code());
                        onvifResponse.setErrorMessage("Response body is empty.");
                    }

                }
            }
        }

        return parse(onvifResponse);
    }

    private static final String KEY_PROFILES = "Profiles";
    private static final String ATTR_TOKEN = "token";
    private static final String ATTR_NAME = "Name";

    public List<OnvifMediaProfile> parse(OnvifResponse response) throws Exception {
        List<OnvifMediaProfile> profiles = new ArrayList<>();
        XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
        xmlFactory.setNamespaceAware(true);
        XmlPullParser xpp = xmlFactory.newPullParser();
        int eventType = 0;
        try {
            xpp.setInput(new StringReader(response.getXml()));
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(KEY_PROFILES)) {

                    String token = xpp.getAttributeValue(null, ATTR_TOKEN);
                    xpp.nextTag();
                    if (xpp.getName().equals(ATTR_NAME)) {
                        xpp.next();
                        String name = xpp.getText();
                        profiles.add(new OnvifMediaProfile(name, token));
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return profiles;
    }

    void sendMoveRequestAndBody(OnvifDevice device, OnvifRequest request, String profileToken, double panSpeed, double tiltSpeed, double zoomSpeed) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(OnvifXMLBuilder.getContinuousMoveBody(profileToken, panSpeed, tiltSpeed, zoomSpeed), this.reqBodyType);
        this.performXmlRequest(device, request, this.buildOnvifRequest(device, request));
    }

    void sendStopRequest(OnvifDevice device, OnvifRequest request, String profileToken, Boolean panTilt, Boolean zoom) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(OnvifXMLBuilder.getStopBody(profileToken, panTilt, zoom), this.reqBodyType);
        this.performXmlRequest(device, request, this.buildOnvifRequest(device, request));
    }

    void sendPullMessageRequest(OnvifDevice device, OnvifRequest request, String subscriptionPolicyUrl, String timeout, int messageLimit) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(OnvifXMLBuilder.getPullMessagesBody(timeout, messageLimit), this.reqBodyType);
        this.performXmlRequest(device, request, this.buildOnvifPullMessageRequest(subscriptionPolicyUrl));
    }

    OnvifResponse sendUnsubscribeRequest(OnvifDevice device, OnvifRequest request, String subscriptionPolicyUrl) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(OnvifXMLBuilder.getUnsubscribeBody(subscriptionPolicyUrl), this.reqBodyType);
        return this.performXmlRequestSynchronous(device, request, this.buildOnvifPullMessageRequest(subscriptionPolicyUrl));
    }

    OnvifResponse sendCreatePullPointSubscription(OnvifDevice device, OnvifRequest request, String filterExpression) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(OnvifXMLBuilder.getCreatePullPointSubscriptionBody(filterExpression, device.getHostName()), this.reqBodyType);
        return this.performXmlRequestSynchronous(device, request, this.buildOnvifRequest(device, request));
    }

    void clear() {
        this.onvifResponseListener = null;
    }

    public void setOnvifResponseListener(OnvifResponseListener onvifResponseListener) {
        this.onvifResponseListener = onvifResponseListener;
    }

    private void performXmlRequest(final OnvifDevice device, final OnvifRequest request, Request xmlRequest) {
        if (xmlRequest != null) {
            this.client.newCall(xmlRequest).enqueue(new Callback() {
                public void onResponse(@NotNull Call call, @NotNull Response xmlResponse) throws IOException {
                    OnvifResponse response = new OnvifResponse(request);
                    ResponseBody xmlBody = xmlResponse.body();
                    if (xmlResponse.code() == 200 && xmlBody != null) {
                        response.setSuccess(true);
                        response.setXml(xmlBody.string());
                        OnvifExecutor.this.parseResponse(device, response);
                    } else {
                        String errorMessage = "";
                        if (xmlBody != null) {
                            errorMessage = xmlBody.string();
                        }

                        OnvifExecutor.this.onvifResponseListener.onError(device, xmlResponse.code(), errorMessage);
                    }

                }

                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    OnvifExecutor.this.onvifResponseListener.onError(device, -1, e.getMessage());
                }
            });
        }
    }

    private OnvifResponse performXmlRequestSynchronous(final OnvifDevice device, final OnvifRequest request, Request xmlRequest) {
        OnvifResponse onvifResponse = new OnvifResponse(request);
        if (xmlRequest != null) {
            try (Response response = this.client.newCall(xmlRequest).execute()) {
                if (response.isSuccessful()) {
                    onvifResponse.setSuccess(true);

                    ResponseBody body = response.body();
                    if (body != null) {
                        // 将响应的 XML 内容设置到 OnvifResponse 中
                        onvifResponse.setXml(body.string());
                    } else {
                        onvifResponse.setErrorCode(response.code());
                        onvifResponse.setErrorMessage("Response body is empty.");
                    }

                } else {
                    // 处理非 200 的响应码，并设置错误信息
                    onvifResponse.setErrorCode(response.code());
                    onvifResponse.setErrorMessage("Request failed with code: " + response.code());
                }

            } catch (IOException e) {
                // 捕获 IO 异常并设置错误信息
                onvifResponse.setErrorCode(-1);
                onvifResponse.setErrorMessage("IOException occurred: " + e.getMessage());
            }
        } else {
            // 当请求对象为 null 时，设置 404 错误码和对应错误信息
            onvifResponse.setErrorCode(404);
            onvifResponse.setErrorMessage("XML request is null.");
        }

        return onvifResponse;
    }


    private void parseResponse(OnvifDevice device, OnvifResponse response) {
        switch (response.request().getType()) {
            case GET_SERVICES:
                OnvifServices path = (new GetServicesParser()).parse(response);
                device.setPath(path);
                ((GetServicesRequest) response.request()).getListener().onServicesReceived(device, path);
                break;
            case GET_DEVICE_INFORMATION:
                ((GetDeviceInformationRequest) response.request()).getListener().onDeviceInformationReceived(device, (new GetDeviceInformationParser()).parse(response));
                break;
//            case GET_MEDIA_PROFILES:
//                ((GetMediaProfilesRequest) response.request()).getListener().onMediaProfilesReceived(device, (new GetMediaProfilesParser()).parse(response));
//                break;
            case GET_STREAM_URI:
                GetMediaStreamRequest streamRequest = (GetMediaStreamRequest) response.request();
                streamRequest.getListener().onMediaStreamURIReceived(device, streamRequest.getMediaProfile(), (new GetMediaStreamParser()).parse(response));
                break;
            case GET_VIDEO_CONFIG:
                GetVideoSourceConfigurationsRequest cfgReq = (GetVideoSourceConfigurationsRequest) response.request();
                cfgReq.getListener().onVideoSourceConfigurationReceived(device, new GetVideoSourceConfigurationsParser().parse(response));
                break;
            case GET_PTZ_STATUS:
                GetStatusRequest gs = (GetStatusRequest) response.request();
                OnvifPTZStatus status = new GetStatusParser().parse(response);
                gs.getListener().onPTZStatus(device, status);
                break;
            case PTZ_ABSOLUTE_MOVE:
                onvifResponseListener.onResponse(device, response);
                break;
            case GET_CONFIGURATIONS:
                ((GetConfigurationsRequest) response.request()).getListener().onConfigurationsReceived(device,
                        new GetConfigurationsParser().parse(response));
                break;
            default:
                this.onvifResponseListener.onResponse(device, response);
        }
    }

    private Request buildOnvifRequest(OnvifDevice device, OnvifRequest request) {
        return (new Request.Builder()).url(this.getUrlForRequest(device, request)).addHeader("Content-Type", "text/xml; charset=utf-8").post(this.reqBody).build();
    }

    private String getUrlForRequest(OnvifDevice device, OnvifRequest request) {
        return device.getHostName() + this.getPathForRequest(device, request);
    }

    private Request buildOnvifPullMessageRequest(String subscriptionPolicyUrl) {
        return (new Request.Builder()).url(subscriptionPolicyUrl).addHeader("Content-Type", "text/xml; charset=utf-8").post(this.reqBody).build();
    }

    private String getPathForRequest(OnvifDevice device, OnvifRequest request) {
        return switch (request.getType()) {
            case GET_DEVICE_INFORMATION -> device.getPath().getDeviceInformationPath();
            case GET_MEDIA_PROFILES, GET_VIDEO_CONFIG -> device.getPath().getProfilesPath();
            case GET_STREAM_URI -> device.getPath().getStreamURIPath();
            case GET_RECORD_URI -> "/onvif/recording_service";
            case GET_REPLAY_URI -> "/onvif/replay_service";
            case GET_PTZ_URI -> "/onvif/ptz_service";
            case GET_IMAGING_URI -> "/onvif/imaging_service";
            case GET_EVENTS_URI -> "/onvif/Events";
            case GET_PTZ_STATUS, PTZ_ABSOLUTE_MOVE, GET_CONFIGURATIONS, GOTO_HOME_POSITION -> device.getPath().getPTZPath();
            default -> device.getPath().getServicesPath();
        };
    }

    private String bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            if (copy.body() != null) {
                copy.body().writeTo(buffer);
            }

            return buffer.readUtf8();
        } catch (IOException var4) {
            var4.printStackTrace();
            return "";
        }
    }
}
