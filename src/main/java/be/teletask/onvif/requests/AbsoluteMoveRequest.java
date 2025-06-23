package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;

public class AbsoluteMoveRequest implements OnvifRequest {

    public static final String TAG = GetServicesRequest.class.getSimpleName();

    private final String token;
    private final double pan, tilt, zoom;

    public AbsoluteMoveRequest(String profileToken, double pan, double tilt, double zoom) {
        super();
        this.token = profileToken;
        this.pan = pan;
        this.tilt = tilt;
        this.zoom = zoom;
    }

    @Override
    public String getXml() {
        return "<AbsoluteMove xmlns=\"http://www.onvif.org/ver20/ptz/wsdl\">" +
                "<ProfileToken>" + token + "</ProfileToken>" +
                "<Position> " +
                "<PanTilt x=\"" + pan + "\" y=\"" + tilt + "\" xmlns=\"http://www.onvif.org/ver10/schema\" />" +
                "<Zoom x=\"" + zoom + "\" xmlns=\"http://www.onvif.org/ver10/schema\" />" +
                "</Position>" +
                "</AbsoluteMove>";
    }

    @Override
    public OnvifType getType() {
        return OnvifType.PTZ_ABSOLUTE_MOVE;
    }
}
