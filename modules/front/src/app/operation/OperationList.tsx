import * as React from "react";

import { observer } from "mobx-react";

import { Modal, Button, Card, Icon, Spin } from "antd";

import { Operation } from "../../cuba/entities/akk$Operation";
import { Link } from "react-router-dom";

import {
  collection,
  injectMainStore,
  MainStoreInjected,
  EntityProperty
} from "@cuba-platform/react";

import { SerializedEntity } from "@cuba-platform/rest";
import { OperationManagement } from "./OperationManagement";
import {
  FormattedMessage,
  injectIntl,
  WrappedComponentProps
} from "react-intl";

@injectMainStore
@observer
class OperationListComponent extends React.Component<
  MainStoreInjected & WrappedComponentProps
> {
  dataCollection = collection<Operation>(Operation.NAME, {
    view: "operation-browse",
    sort: "-updateTs"
  });

  fields = [
    "id",

    "version",

    "createTs",

    "createdBy",

    "updateTs",

    "updatedBy",

    "deleteTs",

    "deletedBy",

    "opType",

    "opDate",

    "amount1",

    "amount2",

    "comments",

    "acc1",

    "acc2",

    "category"
  ];

  showDeletionDialog = (e: SerializedEntity<Operation>) => {
    Modal.confirm({
      title: this.props.intl.formatMessage(
        { id: "management.browser.delete.areYouSure" },
        { instanceName: e._instanceName }
      ),
      okText: this.props.intl.formatMessage({
        id: "management.browser.delete.ok"
      }),
      cancelText: this.props.intl.formatMessage({
        id: "management.browser.delete.cancel"
      }),
      onOk: () => {
        return this.dataCollection.delete(e);
      }
    });
  };

  render() {
    const { status, items } = this.dataCollection;

    if (status === "LOADING") {
      return (
        <div
          style={{
            position: "absolute",
            left: "50%",
            top: "50%",
            transform: "translate(-50%, -50%)"
          }}
        >
          <Spin size="large" />
        </div>
      );
    }

    return (
      <div className="narrow-layout">
        <div style={{ marginBottom: "12px" }}>
          <Link
            to={
              OperationManagement.PATH + "/" + OperationManagement.NEW_SUBPATH
            }
          >
            <Button htmlType="button" type="primary" icon="plus">
              <span>
                <FormattedMessage id="management.browser.create" />
              </span>
            </Button>
          </Link>
        </div>

        {items == null || items.length === 0 ? (
          <p>
            <FormattedMessage id="management.browser.noItems" />
          </p>
        ) : null}
        {items.map(e => (
          <Card
            title={e._instanceName}
            key={e.id}
            style={{ marginBottom: "12px" }}
            actions={[
              <Icon
                type="delete"
                key="delete"
                onClick={() => this.showDeletionDialog(e)}
              />,
              <Link to={OperationManagement.PATH + "/" + e.id} key="edit">
                <Icon type="edit" />
              </Link>
            ]}
          >
            {this.fields.map(p => (
              <EntityProperty
                entityName={Operation.NAME}
                propertyName={p}
                value={e[p]}
                key={p}
              />
            ))}
          </Card>
        ))}
      </div>
    );
  }
}

const OperationList = injectIntl(OperationListComponent);

export default OperationList;
