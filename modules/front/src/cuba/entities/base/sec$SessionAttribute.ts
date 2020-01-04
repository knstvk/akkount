import { StandardEntity } from "./sys$StandardEntity";
import { Group } from "./sec$Group";
export class SessionAttribute extends StandardEntity {
  static NAME = "sec$SessionAttribute";
  name?: string | null;
  stringValue?: string | null;
  datatype?: string | null;
  group?: Group | null;
  datatypeCaption?: string | null;
}
export type SessionAttributeViewName = "_minimal" | "_local" | "_base" | "edit";
export type SessionAttributeView<
  V extends SessionAttributeViewName
> = V extends "_local"
  ? Pick<
      SessionAttribute,
      "id" | "name" | "stringValue" | "datatype" | "datatypeCaption"
    >
  : V extends "_base"
  ? Pick<
      SessionAttribute,
      "id" | "name" | "stringValue" | "datatype" | "datatypeCaption"
    >
  : V extends "edit"
  ? Pick<
      SessionAttribute,
      "id" | "name" | "stringValue" | "datatype" | "datatypeCaption" | "group"
    >
  : never;
