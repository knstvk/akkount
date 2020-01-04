import { User } from "./sec$User";
import { Presentation } from "./sec$Presentation";
export class SearchFolder {
  static NAME = "sec$SearchFolder";
  user?: User | null;
  presentation?: Presentation | null;
  isSet?: boolean | null;
  entityType?: string | null;
}
