package org.smarthome.domain.illumination;

public class IlluminationOn extends IlluminationState {

    public IlluminationOn(Illumination illumination) {
        super(illumination);
    }

    @Override
    public void handle() {
        illumination.setIlluminationState(new IlluminationOff(illumination));
        for (Light light : illumination.getLights()) {
            light.setLightState(new LightOff(light));
        }
    }

}
