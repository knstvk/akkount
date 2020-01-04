import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class CategoryAttributeEnumValue extends BaseUuidEntity {
  static NAME = "sys$CategoryAttributeEnumValue";
  value?: string | null;
  localizedValues?: string | null;
}
export type CategoryAttributeEnumValueViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type CategoryAttributeEnumValueView<
  V extends CategoryAttributeEnumValueViewName
> = never;
