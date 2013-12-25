package akkount.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum OperationType implements EnumClass<String>{

    EXPENSE("E"),
    INCOME("I"),
    TRANSFER("T"),
    DEBT("D");

    private String id;

    OperationType (String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    public static OperationType fromId(String id) {
        for (OperationType at : OperationType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}