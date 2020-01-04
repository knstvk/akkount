import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class ManagedBeanOperation extends BaseUuidEntity {
  static NAME = "jmxcontrol$ManagedBeanOperation";
  name?: string | null;
  returnType?: string | null;
  description?: string | null;
  runAsync?: boolean | null;
  timeout?: any | null;
}
export type ManagedBeanOperationViewName = "_minimal" | "_local" | "_base";
export type ManagedBeanOperationView<
  V extends ManagedBeanOperationViewName
> = never;
