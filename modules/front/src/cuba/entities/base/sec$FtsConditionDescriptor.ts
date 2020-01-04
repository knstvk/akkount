import { AbstractConditionDescriptor } from "./sec$AbstractConditionDescriptor";
export class FtsConditionDescriptor extends AbstractConditionDescriptor {
  static NAME = "sec$FtsConditionDescriptor";
}
export type FtsConditionDescriptorViewName = "_minimal" | "_local" | "_base";
export type FtsConditionDescriptorView<
  V extends FtsConditionDescriptorViewName
> = never;
