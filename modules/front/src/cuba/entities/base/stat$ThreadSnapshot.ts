import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class ThreadSnapshot extends BaseUuidEntity {
  static NAME = "stat$ThreadSnapshot";
  name?: string | null;
  status?: string | null;
  cpu?: any | null;
  deadLocked?: boolean | null;
  stackTrace?: string | null;
}
export type ThreadSnapshotViewName = "_minimal" | "_local" | "_base";
export type ThreadSnapshotView<V extends ThreadSnapshotViewName> = never;
