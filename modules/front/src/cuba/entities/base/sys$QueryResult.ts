import { BaseIdentityIdEntity } from "./sys$BaseIdentityIdEntity";
export class QueryResult extends BaseIdentityIdEntity {
  static NAME = "sys$QueryResult";
  sessionId?: any | null;
  queryKey?: number | null;
  entityId?: any | null;
  stringEntityId?: string | null;
  intEntityId?: number | null;
  longEntityId?: any | null;
}
export type QueryResultViewName = "_minimal" | "_local" | "_base";
export type QueryResultView<V extends QueryResultViewName> = V extends "_local"
  ? Pick<
      QueryResult,
      | "id"
      | "sessionId"
      | "queryKey"
      | "entityId"
      | "stringEntityId"
      | "intEntityId"
      | "longEntityId"
    >
  : V extends "_base"
  ? Pick<
      QueryResult,
      | "id"
      | "sessionId"
      | "queryKey"
      | "entityId"
      | "stringEntityId"
      | "intEntityId"
      | "longEntityId"
    >
  : never;
