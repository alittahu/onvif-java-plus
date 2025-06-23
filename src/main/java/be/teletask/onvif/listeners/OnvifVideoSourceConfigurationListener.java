package be.teletask.onvif.listeners;

import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifVideoSourceConfiguration;

public interface OnvifVideoSourceConfigurationListener {
    void onVideoSourceConfigurationReceived(OnvifDevice onvifDevice, OnvifVideoSourceConfiguration videoSourceConfiguration);
}
