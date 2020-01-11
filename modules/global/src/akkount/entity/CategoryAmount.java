package akkount.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;

import java.math.BigDecimal;

@MetaClass(name = "akk_CategoryBalance")
public class CategoryAmount extends AbstractNotPersistentEntity {

    @MetaProperty
    private Category category;

    @MetaProperty
    private BigDecimal amount;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
