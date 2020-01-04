import { StandardEntity } from "./sys$StandardEntity";
export class Folder extends StandardEntity {
  static NAME = "sys$Folder";
  parent?: Folder | null;
  name?: string | null;
  sortOrder?: number | null;
  tabName?: string | null;
}
export type FolderViewName = "_minimal" | "_local" | "_base";
export type FolderView<V extends FolderViewName> = V extends "_local"
  ? Pick<Folder, "id" | "name" | "sortOrder" | "tabName">
  : V extends "_base"
  ? Pick<Folder, "id" | "name" | "sortOrder" | "tabName">
  : never;
