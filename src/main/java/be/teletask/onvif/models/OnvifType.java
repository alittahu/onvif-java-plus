//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package be.teletask.onvif.models;
// Modified by Boj on 20241103
// This file includes modifications based on the original project ONVIF-java.
public enum OnvifType {
    CUSTOM(""),
    GET_SERVICES("http://www.onvif.org/ver10/device/wsdl"),
    GET_DEVICE_INFORMATION("http://www.onvif.org/ver10/device/wsdl"),
    GET_MEDIA_PROFILES("http://www.onvif.org/ver10/media/wsdl"),
    GET_STREAM_URI("http://www.onvif.org/ver10/media/wsdl"),
    GET_RECORD_URI("http://www.onvif.org/ver10/recording/wsdl"),
    GET_REPLAY_URI("http://www.onvif.org/ver10/replay/wsdl"),
    GET_IMAGING_URI("http://www.onvif.org/ver10/imaging/wsdl"),
    GET_PTZ_URI("http://www.onvif.org/ver10/ptz/wsdl"),
    GET_EVENTS_URI("http://www.onvif.org/ver10/events/wsdl"),
    GET_EVENTS_PULL_MESSAGE_URI("http://www.onvif.org/ver10/events/wsdl");

    public final String namespace;

    private OnvifType(String namespace) {
        this.namespace = namespace;
    }
}
