package org.smarthome.domain.cleaning;

import org.smarthome.exception.CleaningException;

public abstract class VacuumState {

    protected Vacuum vacuum;

    protected VacuumState(Vacuum vacuum) {
        this.vacuum = vacuum;
    }

    public abstract void clean() throws CleaningException;

}
