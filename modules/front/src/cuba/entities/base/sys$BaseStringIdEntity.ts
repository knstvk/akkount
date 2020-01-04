import { BaseGenericIdEntity } from "./sys$BaseGenericIdEntity";
export class BaseStringIdEntity extends BaseGenericIdEntity {
  static NAME = "sys$BaseStringIdEntity";
}
export type BaseStringIdEntityViewName = "_minimal" | "_local" | "_base";
export type BaseStringIdEntityView<
  V extends BaseStringIdEntityViewName
> = never;
