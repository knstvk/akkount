import { BaseGenericIdEntity } from "./sys$BaseGenericIdEntity";
export class BaseUuidEntity extends BaseGenericIdEntity {
  static NAME = "sys$BaseUuidEntity";
  id?: any | null;
}
export type BaseUuidEntityViewName = "_minimal" | "_local" | "_base";
export type BaseUuidEntityView<V extends BaseUuidEntityViewName> = never;
