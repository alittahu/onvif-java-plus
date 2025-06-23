package be.teletask.onvif.listeners;

import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifPTZStatus;

public interface OnvifPTZStatusListener {
    void onPTZStatus(OnvifDevice device, OnvifPTZStatus status);
}
