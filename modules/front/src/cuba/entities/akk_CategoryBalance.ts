import { AbstractNotPersistentEntity } from "./base/sys$AbstractNotPersistentEntity";
import { Category } from "./akk_Category";
export class CategoryAmount extends AbstractNotPersistentEntity {
  static NAME = "akk_CategoryBalance";
  category?: Category | null;
  amount?: any | null;
}
export type CategoryAmountViewName = "_minimal" | "_local" | "_base";
export type CategoryAmountView<V extends CategoryAmountViewName> = never;
