package org.smarthome.domain.illumination;

public class IlluminationOff extends IlluminationState {

    public IlluminationOff(Illumination illumination) {
        super(illumination);
    }

    @Override
    public void handle() {
        illumination.setIlluminationState(new IlluminationOn(illumination));
        for (Light light : illumination.getLights()) {
            light.setLightState(new LightOn(light));
        }
    }

}
