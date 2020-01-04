import { BaseGenericIdEntity } from "./sys$BaseGenericIdEntity";
export class BaseIntegerIdEntity extends BaseGenericIdEntity {
  static NAME = "sys$BaseIntegerIdEntity";
  id?: number | null;
}
export type BaseIntegerIdEntityViewName = "_minimal" | "_local" | "_base";
export type BaseIntegerIdEntityView<
  V extends BaseIntegerIdEntityViewName
> = never;
