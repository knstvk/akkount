import { AbstractPermissionTarget } from "./sec$AbstractTarget";
import { AttributeTarget } from "./sec$AttributeTarget";
export class MultiplePermissionTarget extends AbstractPermissionTarget {
  static NAME = "sec$MultipleTarget";
  permissions?: AttributeTarget | null;
  localName?: string | null;
  entityMetaClassName?: string | null;
  permissionsInfo?: string | null;
}
export type MultiplePermissionTargetViewName = "_minimal" | "_local" | "_base";
export type MultiplePermissionTargetView<
  V extends MultiplePermissionTargetViewName
> = never;
