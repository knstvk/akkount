import { AbstractConditionDescriptor } from "./sec$AbstractConditionDescriptor";
export class PropertyConditionDescriptor extends AbstractConditionDescriptor {
  static NAME = "sec$PropertyConditionDescriptor";
}
export type PropertyConditionDescriptorViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type PropertyConditionDescriptorView<
  V extends PropertyConditionDescriptorViewName
> = never;
