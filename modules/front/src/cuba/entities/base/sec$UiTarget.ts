import { AbstractPermissionTarget } from "./sec$AbstractTarget";
export class UiPermissionTarget extends AbstractPermissionTarget {
  static NAME = "sec$UiTarget";
  permissionVariant?: any | null;
  screen?: string | null;
  component?: string | null;
}
export type UiPermissionTargetViewName = "_minimal" | "_local" | "_base";
export type UiPermissionTargetView<
  V extends UiPermissionTargetViewName
> = never;
