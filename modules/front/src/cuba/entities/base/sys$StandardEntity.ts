import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class StandardEntity extends BaseUuidEntity {
  static NAME = "sys$StandardEntity";
  version?: number | null;
  createTs?: any | null;
  createdBy?: string | null;
  updateTs?: any | null;
  updatedBy?: string | null;
  deleteTs?: any | null;
  deletedBy?: string | null;
}
export type StandardEntityViewName = "_minimal" | "_local" | "_base";
export type StandardEntityView<V extends StandardEntityViewName> = never;
