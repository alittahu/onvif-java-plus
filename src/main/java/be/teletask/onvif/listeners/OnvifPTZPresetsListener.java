package be.teletask.onvif.listeners;

import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.models.OnvifPTZPreset;

import java.util.List;

public interface OnvifPTZPresetsListener {
    void onPresetsReceived(OnvifDevice device, List<OnvifPTZPreset> presets);
}
