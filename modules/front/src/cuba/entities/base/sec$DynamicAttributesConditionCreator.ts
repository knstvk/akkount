import { AbstractConditionDescriptor } from "./sec$AbstractConditionDescriptor";
export class DynamicAttributesConditionCreator extends AbstractConditionDescriptor {
  static NAME = "sec$DynamicAttributesConditionCreator";
}
export type DynamicAttributesConditionCreatorViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type DynamicAttributesConditionCreatorView<
  V extends DynamicAttributesConditionCreatorViewName
> = never;
