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
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifMediaProfile;
import be.teletask.onvif.requests.GetDeviceInformationRequest;
import be.teletask.onvif.requests.GetMediaProfilesRequest;
import be.teletask.onvif.requests.GetMediaStreamRequest;
import be.teletask.onvif.requests.GetServicesRequest;
import be.teletask.onvif.requests.OnvifRequest;
import be.teletask.onvif.responses.OnvifResponse;
import okhttp3.RequestBody;

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

    public OnvifResponse sendCreatePullPointSubscription(OnvifDevice device, OnvifRequest request, String filterExpression) {
        return this.executor.sendCreatePullPointSubscription(device, request, filterExpression);
    }

    public void sendPullMessageRequest(OnvifDevice device, OnvifRequest request, String subscriptionPolicyUrl, String timeout, int messageLimit) {
        this.executor.sendPullMessageRequest(device, request, subscriptionPolicyUrl, timeout, messageLimit);
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
