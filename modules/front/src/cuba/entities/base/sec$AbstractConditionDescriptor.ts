import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class AbstractConditionDescriptor extends BaseUuidEntity {
  static NAME = "sec$AbstractConditionDescriptor";
  locCaption?: string | null;
  treeCaption?: string | null;
}
export type AbstractConditionDescriptorViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type AbstractConditionDescriptorView<
  V extends AbstractConditionDescriptorViewName
> = never;
