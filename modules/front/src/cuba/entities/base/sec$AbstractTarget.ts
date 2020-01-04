export class AbstractPermissionTarget {
  static NAME = "sec$AbstractTarget";
  id?: string | null;
  caption?: string | null;
  permissionValue?: string | null;
}
export type AbstractPermissionTargetViewName = "_minimal" | "_local" | "_base";
export type AbstractPermissionTargetView<
  V extends AbstractPermissionTargetViewName
> = never;
