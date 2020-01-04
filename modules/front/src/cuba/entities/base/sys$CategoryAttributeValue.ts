import { StandardEntity } from "./sys$StandardEntity";
import { CategoryAttribute } from "./sys$CategoryAttribute";
export class CategoryAttributeValue extends StandardEntity {
  static NAME = "sys$CategoryAttributeValue";
  categoryAttribute?: CategoryAttribute | null;
  code?: string | null;
  stringValue?: string | null;
  intValue?: number | null;
  doubleValue?: any | null;
  decimalValue?: any | null;
  booleanValue?: boolean | null;
  dateValue?: any | null;
  dateWithoutTimeValue?: any | null;
  entity?: any | null;
  entityValue?: any | null;
  childValues?: CategoryAttributeValue[] | null;
  parent?: CategoryAttributeValue | null;
}
export type CategoryAttributeValueViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "categoryAttributeValue";
export type CategoryAttributeValueView<
  V extends CategoryAttributeValueViewName
> = V extends "_local"
  ? Pick<
      CategoryAttributeValue,
      | "id"
      | "code"
      | "stringValue"
      | "intValue"
      | "doubleValue"
      | "decimalValue"
      | "booleanValue"
      | "dateValue"
      | "dateWithoutTimeValue"
    >
  : V extends "_base"
  ? Pick<
      CategoryAttributeValue,
      | "id"
      | "code"
      | "stringValue"
      | "intValue"
      | "doubleValue"
      | "decimalValue"
      | "booleanValue"
      | "dateValue"
      | "dateWithoutTimeValue"
    >
  : V extends "categoryAttributeValue"
  ? Pick<
      CategoryAttributeValue,
      | "id"
      | "code"
      | "stringValue"
      | "intValue"
      | "doubleValue"
      | "decimalValue"
      | "booleanValue"
      | "dateValue"
      | "dateWithoutTimeValue"
      | "categoryAttribute"
    >
  : never;
