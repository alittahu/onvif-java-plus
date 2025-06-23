package be.teletask.onvif.listeners;

import be.teletask.onvif.models.OnvifConfiguration;
import be.teletask.onvif.models.OnvifDevice;

import java.util.List;

public interface OnvifConfigurationsListener {

    void onConfigurationsReceived(OnvifDevice device, List<OnvifConfiguration> onvifConfigurations);

}
