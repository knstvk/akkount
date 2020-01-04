import { AbstractConditionDescriptor } from "./sec$AbstractConditionDescriptor";
export class HeaderConditionDescriptor extends AbstractConditionDescriptor {
  static NAME = "sec$HeaderConditionDescriptor";
}
export type HeaderConditionDescriptorViewName = "_minimal" | "_local" | "_base";
export type HeaderConditionDescriptorView<
  V extends HeaderConditionDescriptorViewName
> = never;
