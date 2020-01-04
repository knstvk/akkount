import { BaseGenericIdEntity } from "./sys$BaseGenericIdEntity";
export class BaseDbGeneratedIdEntity extends BaseGenericIdEntity {
  static NAME = "sys$BaseDbGeneratedIdEntity";
}
export type BaseDbGeneratedIdEntityViewName = "_minimal" | "_local" | "_base";
export type BaseDbGeneratedIdEntityView<
  V extends BaseDbGeneratedIdEntityViewName
> = never;
