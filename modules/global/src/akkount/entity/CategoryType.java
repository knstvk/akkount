package akkount.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

public enum CategoryType implements EnumClass<String>{

    EXPENSE("E"),
    INCOME("I");

    private String id;

    CategoryType (String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    public static CategoryType fromId(String id) {
        for (CategoryType at : CategoryType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}