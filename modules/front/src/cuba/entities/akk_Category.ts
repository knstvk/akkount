import { StandardEntity } from "./base/sys$StandardEntity";
import { CategoryType } from "../enums/enums";
export class Category extends StandardEntity {
  static NAME = "akk_Category";
  name?: string | null;
  description?: string | null;
  catType?: CategoryType | null;
}
export type CategoryViewName = "_minimal" | "_local" | "_base";
export type CategoryView<V extends CategoryViewName> = V extends "_minimal"
  ? Pick<Category, "id" | "name">
  : V extends "_local"
  ? Pick<Category, "id" | "name" | "description" | "catType">
  : V extends "_base"
  ? Pick<Category, "id" | "name" | "description" | "catType">
  : never;
