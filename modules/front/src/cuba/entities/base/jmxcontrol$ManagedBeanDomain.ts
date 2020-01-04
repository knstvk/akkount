import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class ManagedBeanDomain extends BaseUuidEntity {
  static NAME = "jmxcontrol$ManagedBeanDomain";
  name?: string | null;
}
export type ManagedBeanDomainViewName = "_minimal" | "_local" | "_base";
export type ManagedBeanDomainView<V extends ManagedBeanDomainViewName> = never;
