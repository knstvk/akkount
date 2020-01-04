import { Folder } from "./sys$Folder";
export class AbstractSearchFolder extends Folder {
  filterComponentId?: string | null;
  filterXml?: string | null;
  applyDefault?: boolean | null;
  locName?: string | null;
}
