import { BaseUuidEntity } from "./sys$BaseUuidEntity";
import { User } from "./sec$User";
export class Presentation extends BaseUuidEntity {
  static NAME = "sec$Presentation";
  createTs?: any | null;
  createdBy?: string | null;
  componentId?: string | null;
  name?: string | null;
  xml?: string | null;
  user?: User | null;
  autoSave?: boolean | null;
  updateTs?: any | null;
  updatedBy?: string | null;
}
export type PresentationViewName = "_minimal" | "_local" | "_base" | "app";
export type PresentationView<
  V extends PresentationViewName
> = V extends "_minimal"
  ? Pick<Presentation, "id" | "name">
  : V extends "_local"
  ? Pick<Presentation, "id" | "componentId" | "name" | "xml" | "autoSave">
  : V extends "_base"
  ? Pick<Presentation, "id" | "name" | "componentId" | "xml" | "autoSave">
  : V extends "app"
  ? Pick<
      Presentation,
      "id" | "componentId" | "name" | "xml" | "user" | "autoSave"
    >
  : never;
