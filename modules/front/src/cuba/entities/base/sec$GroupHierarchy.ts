import { BaseUuidEntity } from "./sys$BaseUuidEntity";
import { Group } from "./sec$Group";
export class GroupHierarchy extends BaseUuidEntity {
  static NAME = "sec$GroupHierarchy";
  createTs?: any | null;
  createdBy?: string | null;
  group?: Group | null;
  parent?: Group | null;
  level?: number | null;
}
export type GroupHierarchyViewName = "_minimal" | "_local" | "_base";
export type GroupHierarchyView<
  V extends GroupHierarchyViewName
> = V extends "_local"
  ? Pick<GroupHierarchy, "id" | "level">
  : V extends "_base"
  ? Pick<GroupHierarchy, "id" | "level">
  : never;
