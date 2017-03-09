package com.neurotec.licensing;


public class LicensingStateResult {

    // ===========================================================
    // Private fields
    // ===========================================================

    private LicensingState mState;
    private Exception mException;

    // ===========================================================
    // Public constructors
    // ===========================================================

    public LicensingStateResult(LicensingState state) {
        this(state, null);
    }

    public LicensingStateResult(LicensingState state, Exception exception) {
        mState = state;
        mException = exception;
    }

    // ===========================================================
    // Public methods
    // ===========================================================

    public LicensingState getState() {
        return mState;
    }

    public Exception getException() {
        return mException;
    }

}