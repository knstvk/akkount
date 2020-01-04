import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class Server extends BaseUuidEntity {
  static NAME = "sys$Server";
  createTs?: any | null;
  createdBy?: string | null;
  updateTs?: any | null;
  updatedBy?: string | null;
  name?: string | null;
  running?: boolean | null;
  data?: string | null;
}
export type ServerViewName = "_minimal" | "_local" | "_base";
export type ServerView<V extends ServerViewName> = V extends "_minimal"
  ? Pick<Server, "id" | "name">
  : V extends "_local"
  ? Pick<Server, "id" | "name" | "running" | "data">
  : V extends "_base"
  ? Pick<Server, "id" | "name" | "running" | "data">
  : never;
