import { StandardEntity } from "./sys$StandardEntity";
export class FileDescriptor extends StandardEntity {
  static NAME = "sys$FileDescriptor";
  name?: string | null;
  extension?: string | null;
  size?: any | null;
  createDate?: any | null;
}
export type FileDescriptorViewName = "_minimal" | "_local" | "_base" | "browse";
export type FileDescriptorView<
  V extends FileDescriptorViewName
> = V extends "_minimal"
  ? Pick<FileDescriptor, "id" | "name" | "createDate" | "extension">
  : V extends "_local"
  ? Pick<FileDescriptor, "id" | "name" | "extension" | "size" | "createDate">
  : V extends "_base"
  ? Pick<FileDescriptor, "id" | "name" | "createDate" | "extension" | "size">
  : V extends "browse"
  ? Pick<FileDescriptor, "id" | "name" | "extension" | "size" | "createDate">
  : never;
