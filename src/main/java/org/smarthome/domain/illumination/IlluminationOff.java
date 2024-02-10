package org.smarthome.domain.illumination;

public class IlluminationOff extends IlluminationState {

    public IlluminationOff(Illumination illumination) {
        super(illumination);
    }

    @Override
    public void handle() {
        illumination.on();
    }

}
