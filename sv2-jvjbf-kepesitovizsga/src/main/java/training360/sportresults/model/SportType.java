package training360.sportresults.model;

public enum SportType {
    SWIMMING('s'), RUNNING('s'), POLE_VAULT('m'), HAMMER_THROWING('m');

    private final char measureUnit;

    SportType(char measureUnit) {
        this.measureUnit = measureUnit;
    }

    public char getMeasureUnit() {
        return measureUnit;
    }
}
