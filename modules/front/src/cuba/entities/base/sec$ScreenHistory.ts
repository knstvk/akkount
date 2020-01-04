import { BaseUuidEntity } from "./sys$BaseUuidEntity";
import { User } from "./sec$User";
export class ScreenHistoryEntity extends BaseUuidEntity {
  static NAME = "sec$ScreenHistory";
  createTs?: any | null;
  createdBy?: string | null;
  user?: User | null;
  substitutedUser?: User | null;
  caption?: string | null;
  url?: string | null;
  entityRef?: any | null;
  displayUser?: string | null;
}
export type ScreenHistoryEntityViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "browse";
export type ScreenHistoryEntityView<
  V extends ScreenHistoryEntityViewName
> = V extends "_local"
  ? Pick<ScreenHistoryEntity, "id" | "caption" | "url" | "displayUser">
  : V extends "_base"
  ? Pick<ScreenHistoryEntity, "id" | "caption" | "url" | "displayUser">
  : V extends "browse"
  ? Pick<
      ScreenHistoryEntity,
      | "id"
      | "caption"
      | "url"
      | "displayUser"
      | "user"
      | "substitutedUser"
      | "createTs"
    >
  : never;
