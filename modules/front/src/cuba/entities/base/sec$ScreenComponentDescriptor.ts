import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class ScreenComponentDescriptor extends BaseUuidEntity {
  static NAME = "sec$ScreenComponentDescriptor";
  parent?: ScreenComponentDescriptor | null;
  caption?: string | null;
}
export type ScreenComponentDescriptorViewName = "_minimal" | "_local" | "_base";
export type ScreenComponentDescriptorView<
  V extends ScreenComponentDescriptorViewName
> = never;
