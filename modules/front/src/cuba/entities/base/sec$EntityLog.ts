import { BaseUuidEntity } from "./sys$BaseUuidEntity";
import { User } from "./sec$User";
import { EntityLogAttr } from "./sec$EntityLogAttr";
export class EntityLogItem extends BaseUuidEntity {
  static NAME = "sec$EntityLog";
  createTs?: any | null;
  createdBy?: string | null;
  eventTs?: any | null;
  user?: User | null;
  type?: any | null;
  entity?: string | null;
  entityRef?: any | null;
  entityInstanceName?: string | null;
  attributes?: EntityLogAttr | null;
  changes?: string | null;
}
export type EntityLogItemViewName = "_minimal" | "_local" | "_base" | "logView";
export type EntityLogItemView<
  V extends EntityLogItemViewName
> = V extends "_local"
  ? Pick<
      EntityLogItem,
      "id" | "eventTs" | "type" | "entity" | "entityInstanceName" | "changes"
    >
  : V extends "_base"
  ? Pick<
      EntityLogItem,
      "id" | "eventTs" | "type" | "entity" | "entityInstanceName" | "changes"
    >
  : V extends "logView"
  ? Pick<
      EntityLogItem,
      | "id"
      | "eventTs"
      | "type"
      | "entity"
      | "entityInstanceName"
      | "changes"
      | "user"
      | "entityRef"
    >
  : never;
