enum TaskType {
    COMPUTATIONAL(1), IO(2), OTHER(3);

    private int TypePriority;

     TaskType(int priority) {
        if (validatePriority(priority)) TypePriority = priority;
        else {
            throw new IllegalArgumentException("Not a valid priority");
        }
    }

    public void setTypePriority(int priority) {
        if (validatePriority(priority)) TypePriority = priority;
        else {
            throw new IllegalArgumentException("Not a valid priority");
        }
    }

    public int getTypePriority() {
        return this.TypePriority;
    }

    public TaskType getType() {
        return this;
    }

    private boolean validatePriority(int priority) {
        if (priority < 1 || priority > 10) return false;
        return true;
    }
}