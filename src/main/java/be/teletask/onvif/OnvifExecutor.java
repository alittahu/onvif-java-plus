//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package be.teletask.onvif;

import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifServices;
import be.teletask.onvif.parsers.GetDeviceInformationParser;
import be.teletask.onvif.parsers.GetMediaProfilesParser;
import be.teletask.onvif.parsers.GetMediaStreamParser;
import be.teletask.onvif.parsers.GetServicesParser;
import be.teletask.onvif.requests.GetDeviceInformationRequest;
import be.teletask.onvif.requests.GetMediaProfilesRequest;
import be.teletask.onvif.requests.GetMediaStreamRequest;
import be.teletask.onvif.requests.GetServicesRequest;
import be.teletask.onvif.requests.OnvifRequest;
import be.teletask.onvif.responses.OnvifResponse;
import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;

import java.io.IOException;
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

public class OnvifExecutor {
    public static final String TAG = OnvifExecutor.class.getSimpleName();
    private OkHttpClient client;
    private MediaType reqBodyType;
    private RequestBody reqBody;
    private Credentials credentials;
    private OnvifResponseListener onvifResponseListener;

    OnvifExecutor(OnvifResponseListener onvifResponseListener) {
        this.onvifResponseListener = onvifResponseListener;
        this.credentials = new Credentials("username", "password");
        DigestAuthenticator authenticator = new DigestAuthenticator(this.credentials);
        Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap();
        this.client = (new OkHttpClient.Builder()).connectTimeout(10000L, TimeUnit.SECONDS).writeTimeout(100L, TimeUnit.SECONDS).readTimeout(10000L, TimeUnit.SECONDS).authenticator(new CachingAuthenticatorDecorator(authenticator, authCache)).addInterceptor(new AuthenticationCacheInterceptor(authCache)).build();
        this.reqBodyType = MediaType.parse("application/soap+xml; charset=utf-8;");
    }

    void sendRequest(OnvifDevice device, OnvifRequest request) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(this.reqBodyType, OnvifXMLBuilder.getSoapHeader() + request.getXml() + OnvifXMLBuilder.getEnvelopeEnd());
        this.performXmlRequest(device, request, this.buildOnvifRequest(device, request));
    }

    void sendMoveRequestAndBody(OnvifDevice device, OnvifRequest request, String profileToken, double panSpeed, double tiltSpeed, double zoomSpeed) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(this.reqBodyType, OnvifXMLBuilder.getContinuousMoveBody(profileToken, panSpeed, tiltSpeed, zoomSpeed));
        this.performXmlRequest(device, request, this.buildOnvifRequest(device, request));
    }

    void sendStopRequest(OnvifDevice device, OnvifRequest request, String profileToken, Boolean panTilt, Boolean zoom) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(this.reqBodyType, OnvifXMLBuilder.getStopBody(profileToken, panTilt, zoom));
        this.performXmlRequest(device, request, this.buildOnvifRequest(device, request));
    }

    void sendPullMessageRequest(OnvifDevice device, OnvifRequest request, String subscriptionPolicyUrl, String timeout, int messageLimit) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(this.reqBodyType, OnvifXMLBuilder.getPullMessagesBody(timeout, messageLimit));
        this.performXmlRequest(device, request, this.buildOnvifPullMessageRequest(subscriptionPolicyUrl));
    }
    OnvifResponse sendUnsubscribeRequest(OnvifDevice device, OnvifRequest request, String subscriptionPolicyUrl) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(this.reqBodyType, OnvifXMLBuilder.getUnsubscribeBody(subscriptionPolicyUrl));
       return this.performXmlRequestSynchronous(device, request, this.buildOnvifPullMessageRequest(subscriptionPolicyUrl));
    }

    OnvifResponse sendCreatePullPointSubscription(OnvifDevice device, OnvifRequest request, String filterExpression) {
        this.credentials.setUserName(device.getUsername());
        this.credentials.setPassword(device.getPassword());
        this.reqBody = RequestBody.create(this.reqBodyType, OnvifXMLBuilder.getCreatePullPointSubscriptionBody(filterExpression, device.getHostName()));
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
                public void onResponse(Call call, Response xmlResponse) throws IOException {
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

                public void onFailure(Call call, IOException e) {
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
            case GET_MEDIA_PROFILES:
                ((GetMediaProfilesRequest) response.request()).getListener().onMediaProfilesReceived(device, (new GetMediaProfilesParser()).parse(response));
                break;
            case GET_STREAM_URI:
                GetMediaStreamRequest streamRequest = (GetMediaStreamRequest) response.request();
                streamRequest.getListener().onMediaStreamURIReceived(device, streamRequest.getMediaProfile(), (new GetMediaStreamParser()).parse(response));
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
        switch (request.getType()) {
            case GET_SERVICES:
                return device.getPath().getServicesPath();
            case GET_DEVICE_INFORMATION:
                return device.getPath().getDeviceInformationPath();
            case GET_MEDIA_PROFILES:
                return device.getPath().getProfilesPath();
            case GET_STREAM_URI:
                return device.getPath().getStreamURIPath();
            case GET_RECORD_URI:
                return "/onvif/recording_service";
            case GET_REPLAY_URI:
                return "/onvif/replay_service";
            case GET_PTZ_URI:
                return "/onvif/ptz_service";
            case GET_IMAGING_URI:
                return "/onvif/imaging_service";
            case GET_EVENTS_URI:
                return "/onvif/Events";
            default:
                return device.getPath().getServicesPath();
        }
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
