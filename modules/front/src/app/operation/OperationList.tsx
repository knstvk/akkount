import * as React from "react";

import {observer} from "mobx-react";

import {Button, Card, Icon, Modal, Pagination, Spin} from "antd";

import {Operation} from "../../cuba/entities/akk$Operation";
import {Link} from "react-router-dom";

import {collection, EntityProperty, injectMainStore, MainStoreInjected} from "@cuba-platform/react";

import {SerializedEntity} from "@cuba-platform/rest";
import {OperationManagement} from "./OperationManagement";
import {FormattedMessage, injectIntl, WrappedComponentProps} from "react-intl";
import {OperationType} from "../../cuba/enums/enums";

const PAGE_SIZE = 20;

@injectMainStore
@observer
class OperationListComponent extends React.Component<MainStoreInjected & WrappedComponentProps> {

  dataCollection = collection<Operation>(Operation.NAME, {
    view: "operation-browse",
    sort: "-createTs",
    limit: PAGE_SIZE
  });

  fields = [
    "id",
    "acc1",
    "amount1",
    "acc2",
    "amount2",
    "category",
    "comments"
  ];

  showDeletionDialog = (e: SerializedEntity<Operation>) => {
    Modal.confirm({
      title: this.props.intl.formatMessage(
        { id: "management.browser.delete.areYouSure" },
        { instanceName: e.opType + " on " + e.opDate }
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
              OperationManagement.PATH + "/" + OperationManagement.NEW_EXPENSE
            }
          >
            <Button htmlType="button" type="primary" style={{margin: "0 12px 12px 0"}}>
              <span>
                Expense
              </span>
            </Button>
          </Link>

          <Link
            to={
              OperationManagement.PATH + "/" + OperationManagement.NEW_INCOME
            }
          >
            <Button htmlType="button" type="primary" style={{margin: "0 12px 12px 0"}}>
              <span>
                Income
              </span>
            </Button>
          </Link>

          <Link
            to={
              OperationManagement.PATH + "/" + OperationManagement.NEW_TRANSFER
            }
          >
            <Button htmlType="button" type="primary" style={{margin: "0 12px 12px 0"}}>
              <span>
                Transfer
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
            title={e.opDate + " - " + e.opType}
            key={e.id}
            style={{ marginBottom: "12px" }}
            actions={[
              <Link to={OperationManagement.PATH + "/" + e.id} key="edit">
                <Icon type="edit" />
              </Link>,
              <Icon
                type="delete"
                key="delete"
                onClick={() => this.showDeletionDialog(e)}
              />
            ]}
          >
            {this.fields
              .filter(p => e.opType === OperationType.TRANSFER
                  || (e.opType === OperationType.INCOME && p !== "amount1")
                  || (e.opType === OperationType.EXPENSE && p !== "amount2"))
              .map(p => (
                <EntityProperty
                  entityName={Operation.NAME}
                  propertyName={p}
                  value={e[p]}
                  key={p}
                />
              ))}
          </Card>
        ))}
        <Pagination style={{ marginBottom: "12px" }}
          onChange={this.onChange}
          defaultPageSize={PAGE_SIZE}
          defaultCurrent={1}
          total={this.dataCollection.count}
          current={this.dataCollection.offset ? this.dataCollection.offset / PAGE_SIZE + 1 : 0 }
        />
      </div>
    );
  }

  onChange = (current: number, size: number) => {
    console.log(current, size);
    this.dataCollection.offset = (current - 1) * size;
    this.dataCollection.load()
  };
}

const OperationList = injectIntl(OperationListComponent);

export default OperationList;
