import { StandardEntity } from "./sys$StandardEntity";
import { CategoryAttribute } from "./sys$CategoryAttribute";
export class Category extends StandardEntity {
  static NAME = "sys$Category";
  name?: string | null;
  entityType?: string | null;
  isDefault?: boolean | null;
  categoryAttrs?: CategoryAttribute[] | null;
  localeNames?: string | null;
  localeName?: string | null;
  special?: string | null;
}
export type CategoryViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "category.edit"
  | "category.defaultEdit"
  | "for.cache";
export type CategoryView<V extends CategoryViewName> = V extends "_minimal"
  ? Pick<Category, "id" | "localeName">
  : V extends "_local"
  ? Pick<
      Category,
      "id" | "name" | "entityType" | "isDefault" | "localeNames" | "special"
    >
  : V extends "_base"
  ? Pick<
      Category,
      | "id"
      | "localeName"
      | "name"
      | "entityType"
      | "isDefault"
      | "localeNames"
      | "special"
    >
  : V extends "category.edit"
  ? Pick<
      Category,
      | "id"
      | "name"
      | "entityType"
      | "isDefault"
      | "localeNames"
      | "special"
      | "categoryAttrs"
    >
  : V extends "category.defaultEdit"
  ? Pick<Category, "id" | "localeName" | "isDefault">
  : V extends "for.cache"
  ? Pick<
      Category,
      | "id"
      | "name"
      | "entityType"
      | "isDefault"
      | "localeNames"
      | "special"
      | "categoryAttrs"
    >
  : never;
