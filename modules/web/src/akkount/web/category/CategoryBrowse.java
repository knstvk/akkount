package akkount.web.category;

import akkount.entity.Category;
import com.haulmont.cuba.gui.screen.*;

@UiController("akk$Category.lookup")
@UiDescriptor("category-browse.xml")
@LookupComponent("categoryTable")
@LoadDataBeforeShow
public class CategoryBrowse extends StandardLookup<Category> {
}