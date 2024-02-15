package org.smarthome.domain.sensor;

/**
 * This interface represents the MAPE (Monitor, Analyze, Plan, Execute) control loop for a sensor.
 * It defines the methods necessary for each phase of the control loop.
 *
 * @param <T> The type of data that the sensor monitors.
 */
public interface MAPEControl<T> {

    /**
     * The monitoring phase of the control loop. It is responsible for detecting and retrieving
     * the current state or value of the monitored data.
     *
     * @return The current state or value of the monitored data.
     */
    T monitor();

    /**
     * The analysis phase of the control loop. It evaluates the detected data to determine
     * whether any significant changes or events have occurred.
     *
     * @param detected The data detected during the monitoring phase.
     * @return {@code true} if the analysis is successful (significant change detected), otherwise {@code false}.
     */
    boolean analyze(T detected);

    /**
     * The planning phase of the control loop. It is responsible for planning actions or strategies
     * based on the detected data. This method can be left empty if no planning is needed.
     *
     * @param detected The data detected during the monitoring phase.
     */
    void plan(T detected);

    /**
     * The execution phase of the control loop. It executes actions or updates based on the detected data.
     *
     * @param detected The data detected during the monitoring phase.
     */
    void execute(T detected);

    /**
     * The main control loop that orchestrates the sequence of monitoring, analysis, planning, and execution.
     * It is typically called periodically to ensure continuous monitoring and control.
     */
    void loop();
}