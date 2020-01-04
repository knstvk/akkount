import { AbstractConditionDescriptor } from "./sec$AbstractConditionDescriptor";
export class CustomConditionDescriptor extends AbstractConditionDescriptor {
  static NAME = "sec$CustomConditionDescriptor";
}
export type CustomConditionDescriptorViewName = "_minimal" | "_local" | "_base";
export type CustomConditionDescriptorView<
  V extends CustomConditionDescriptorViewName
> = never;
