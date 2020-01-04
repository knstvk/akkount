import { StandardEntity } from "./sys$StandardEntity";
import { User } from "./sec$User";
export class SessionLogEntry extends StandardEntity {
  static NAME = "sec$SessionLogEntry";
  sessionId?: any | null;
  substitutedUser?: User | null;
  user?: User | null;
  userData?: string | null;
  lastAction?: any | null;
  clientInfo?: string | null;
  address?: string | null;
  startedTs?: any | null;
  finishedTs?: any | null;
  clientType?: any | null;
  server?: string | null;
}
export type SessionLogEntryViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "sessionLogEntry-view";
export type SessionLogEntryView<
  V extends SessionLogEntryViewName
> = V extends "_local"
  ? Pick<
      SessionLogEntry,
      | "id"
      | "sessionId"
      | "userData"
      | "lastAction"
      | "clientInfo"
      | "address"
      | "startedTs"
      | "finishedTs"
      | "clientType"
      | "server"
    >
  : V extends "_base"
  ? Pick<
      SessionLogEntry,
      | "id"
      | "sessionId"
      | "userData"
      | "lastAction"
      | "clientInfo"
      | "address"
      | "startedTs"
      | "finishedTs"
      | "clientType"
      | "server"
    >
  : V extends "sessionLogEntry-view"
  ? Pick<
      SessionLogEntry,
      | "id"
      | "sessionId"
      | "userData"
      | "lastAction"
      | "clientInfo"
      | "address"
      | "startedTs"
      | "finishedTs"
      | "clientType"
      | "server"
      | "user"
      | "substitutedUser"
    >
  : never;
