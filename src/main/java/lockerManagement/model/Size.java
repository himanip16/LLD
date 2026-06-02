package lockerManagement.model;

public enum Size {
    SMALL, MEDIUM, LARGE;

    public Size next() {
        switch (this) {
            case SMALL:  return MEDIUM;
            case MEDIUM: return LARGE;
            case LARGE:  return null;   // no bigger size
            default:     return null;
        }
    }
}