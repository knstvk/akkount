import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class InfoParamEntity extends BaseUuidEntity {
  static NAME = "sys$InfoParamEntity";
  key?: string | null;
  keyValue?: string | null;
}
export type InfoParamEntityViewName = "_minimal" | "_local" | "_base";
export type InfoParamEntityView<
  V extends InfoParamEntityViewName
> = V extends "_minimal"
  ? Pick<InfoParamEntity, "id" | "keyValue">
  : V extends "_base"
  ? Pick<InfoParamEntity, "id" | "keyValue">
  : never;
