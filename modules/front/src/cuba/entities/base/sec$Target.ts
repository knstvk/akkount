import { AbstractPermissionTarget } from "./sec$AbstractTarget";
export class BasicPermissionTarget extends AbstractPermissionTarget {
  static NAME = "sec$Target";
  permissionVariant?: any | null;
}
export type BasicPermissionTargetViewName = "_minimal" | "_local" | "_base";
export type BasicPermissionTargetView<
  V extends BasicPermissionTargetViewName
> = never;
