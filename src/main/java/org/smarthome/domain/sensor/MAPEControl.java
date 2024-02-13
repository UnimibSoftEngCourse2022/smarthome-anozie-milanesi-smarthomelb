package org.smarthome.domain.sensor;

public interface MAPEControl<T> {
    void loop();
    T monitor();
    /**
     *@return {@code true} if analyze if the analysis is successful, otherwise {@code false}
     */
    boolean analyze(T detected);
    void plan();
    void execute();
}
