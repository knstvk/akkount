import { AbstractCondition } from "./sec$AbstractCondition";
export class DynamicAttributesCondition extends AbstractCondition {
  static NAME = "sec$DynamicAttributesCondition";
}
export type DynamicAttributesConditionViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type DynamicAttributesConditionView<
  V extends DynamicAttributesConditionViewName
> = never;
