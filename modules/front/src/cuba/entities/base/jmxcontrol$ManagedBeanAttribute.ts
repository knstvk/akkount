import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class ManagedBeanAttribute extends BaseUuidEntity {
  static NAME = "jmxcontrol$ManagedBeanAttribute";
  name?: string | null;
  description?: string | null;
  type?: string | null;
  readableWriteable?: string | null;
  readable?: boolean | null;
  writeable?: boolean | null;
  valueString?: string | null;
}
export type ManagedBeanAttributeViewName = "_minimal" | "_local" | "_base";
export type ManagedBeanAttributeView<
  V extends ManagedBeanAttributeViewName
> = never;
