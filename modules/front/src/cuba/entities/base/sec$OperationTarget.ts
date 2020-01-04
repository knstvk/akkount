import { AbstractPermissionTarget } from "./sec$AbstractTarget";
export class OperationPermissionTarget extends AbstractPermissionTarget {
  static NAME = "sec$OperationTarget";
  createPermissionVariant?: any | null;
  readPermissionVariant?: any | null;
  updatePermissionVariant?: any | null;
  deletePermissionVariant?: any | null;
  localName?: string | null;
  entityMetaClassName?: string | null;
}
export type OperationPermissionTargetViewName = "_minimal" | "_local" | "_base";
export type OperationPermissionTargetView<
  V extends OperationPermissionTargetViewName
> = never;
