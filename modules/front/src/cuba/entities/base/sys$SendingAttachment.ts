import { StandardEntity } from "./sys$StandardEntity";
import { SendingMessage } from "./sys$SendingMessage";
import { FileDescriptor } from "./sys$FileDescriptor";
export class SendingAttachment extends StandardEntity {
  static NAME = "sys$SendingAttachment";
  message?: SendingMessage | null;
  content?: any | null;
  contentFile?: FileDescriptor | null;
  name?: string | null;
  contentId?: string | null;
  disposition?: string | null;
  encoding?: string | null;
}
export type SendingAttachmentViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "sendingAttachment.loadFromQueue"
  | "sendingAttachment.browse";
export type SendingAttachmentView<
  V extends SendingAttachmentViewName
> = V extends "_local"
  ? Pick<
      SendingAttachment,
      "id" | "content" | "name" | "contentId" | "disposition" | "encoding"
    >
  : V extends "_base"
  ? Pick<
      SendingAttachment,
      "id" | "content" | "name" | "contentId" | "disposition" | "encoding"
    >
  : V extends "sendingAttachment.loadFromQueue"
  ? Pick<
      SendingAttachment,
      | "id"
      | "version"
      | "createTs"
      | "createdBy"
      | "updateTs"
      | "updatedBy"
      | "deleteTs"
      | "deletedBy"
      | "content"
      | "name"
      | "contentId"
      | "disposition"
      | "encoding"
      | "contentFile"
    >
  : V extends "sendingAttachment.browse"
  ? Pick<
      SendingAttachment,
      | "id"
      | "content"
      | "name"
      | "contentId"
      | "disposition"
      | "encoding"
      | "updateTs"
    >
  : never;
