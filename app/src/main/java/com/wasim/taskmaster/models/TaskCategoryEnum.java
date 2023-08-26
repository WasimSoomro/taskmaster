package com.wasim.taskmaster.models;

public enum TaskCategoryEnum {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete"),
    ;

    private String text;

    TaskCategoryEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static TaskCategoryEnum fromString(String text) {
        for (TaskCategoryEnum tce : TaskCategoryEnum.values()) {
            if (tce.text.equalsIgnoreCase(text)) {
                return tce;
            }
        }
        return null;
    }

//    TaskCategoryEnum(String s) {
//
//    }
}

