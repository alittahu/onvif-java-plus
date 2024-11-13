package be.teletask.onvif.parsers;

import be.teletask.onvif.DiscoveryMode;
import be.teletask.onvif.OnvifUtils;
import be.teletask.onvif.models.Device;
import be.teletask.onvif.models.DiscoveryType;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.responses.OnvifResponse;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas Verhelst on 04/09/2018.
 * Copyright (c) 2018 TELETASK BVBA. All rights reserved.
 */

// Modified by Boj on 20241103
// This file includes modifications based on the original project ONVIF-java.
public class DiscoveryParser extends OnvifParser<List<Device>> {

    //Constants
    public static final String TAG = DiscoveryParser.class.getSimpleName();
    private static final String LINE_END = "\r\n";

    //Attributes
    private DiscoveryMode mode;
    private String hostName;

    //Constructors
    public DiscoveryParser(DiscoveryMode mode) {
        this.mode = mode;
        hostName = "";
    }

    //Methods

    @Override
    public List<Device> parse(OnvifResponse response) {
        List<Device> devices = new ArrayList<>();

        switch (mode) {
            case ONVIF:
                devices.addAll(parseOnvif(response));
                break;
            case UPNP:
                break;
        }

        return devices;
    }

    private List<Device> parseOnvif(OnvifResponse response) {
        ArrayList<Device> devices = new ArrayList<>();
        try {
            getXpp().setInput(new StringReader(response.getXml()));
            eventType = getXpp().getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("Types")) {
                    getXpp().next();
                    String type = getXpp().getText();

                    if (mode.equals(DiscoveryMode.ONVIF) && type.contains(DiscoveryType.NETWORK_VIDEO_TRANSMITTER.type)) {
                        String uri = OnvifUtils.retrieveXAddrs(getXpp());
                        devices.addAll(parseDevicesFromUri(uri));
                    }
                }

                eventType = getXpp().next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return devices;
    }



    //Properties

    private String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    private List<OnvifDevice> parseDevicesFromUri(String uri) {
        List<OnvifDevice> devices = new ArrayList<>();
        String[] uris = uri.split("\\s+");
        for (String address : uris) {
            OnvifDevice device = new OnvifDevice(getHostName());
            device.addAddress(address);
            devices.add(device);
        }

        return devices;
    }



}
