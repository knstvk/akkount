import { BaseUuidEntity } from "./sys$BaseUuidEntity";
import { User } from "./sec$User";
export class EntitySnapshot extends BaseUuidEntity {
  static NAME = "sys$EntitySnapshot";
  createTs?: any | null;
  createdBy?: string | null;
  viewXml?: string | null;
  snapshotXml?: string | null;
  entityMetaClass?: string | null;
  snapshotDate?: any | null;
  author?: User | null;
  entity?: any | null;
  label?: string | null;
  changeDate?: any | null;
}
export type EntitySnapshotViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "entitySnapshot.browse";
export type EntitySnapshotView<
  V extends EntitySnapshotViewName
> = V extends "_local"
  ? Pick<
      EntitySnapshot,
      | "id"
      | "viewXml"
      | "snapshotXml"
      | "entityMetaClass"
      | "snapshotDate"
      | "label"
      | "changeDate"
    >
  : V extends "_base"
  ? Pick<
      EntitySnapshot,
      | "id"
      | "viewXml"
      | "snapshotXml"
      | "entityMetaClass"
      | "snapshotDate"
      | "label"
      | "changeDate"
    >
  : V extends "entitySnapshot.browse"
  ? Pick<
      EntitySnapshot,
      | "id"
      | "viewXml"
      | "snapshotXml"
      | "entityMetaClass"
      | "snapshotDate"
      | "label"
      | "changeDate"
      | "createTs"
      | "author"
    >
  : never;
