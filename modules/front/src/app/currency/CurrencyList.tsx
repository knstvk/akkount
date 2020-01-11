import * as React from "react";

import {observer} from "mobx-react";

import {Button, Icon, List, Modal, Spin} from "antd";

import {Currency} from "../../cuba/entities/akk_Currency";
import {Link} from "react-router-dom";

import {collection, EntityProperty, injectMainStore, MainStoreInjected} from "@cuba-platform/react";

import {SerializedEntity} from "@cuba-platform/rest";
import {CurrencyManagement} from "./CurrencyManagement";
import {injectIntl, WrappedComponentProps} from "react-intl";

@injectMainStore
@observer
class CurrencyListComponent extends React.Component<MainStoreInjected & WrappedComponentProps> {

  dataCollection = collection<Currency>(Currency.NAME, {
    view: "_local",
    sort: "code"
  });

  fields = ["code", "name"];

  showDeletionDialog = (e: SerializedEntity<Currency>) => {
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
            to={CurrencyManagement.PATH + "/" + CurrencyManagement.NEW_SUBPATH}
          >
            <Button htmlType="button" type="primary" icon="plus">
              <span>
                Create Currency
              </span>
            </Button>
          </Link>
        </div>

        <List
          itemLayout="horizontal"
          bordered
          dataSource={items}
          renderItem={item => (
            <List.Item
              actions={[
                <Link to={CurrencyManagement.PATH + "/" + item.id} key="edit">
                  <Icon type="edit" />
                </Link>,
                <Icon
                  type="delete"
                  key="delete"
                  onClick={() => this.showDeletionDialog(item)}
                />
              ]}
            >
              <div style={{ flexGrow: 1 }}>
                {this.fields.map(p => (
                  <EntityProperty
                    entityName={Currency.NAME}
                    propertyName={p}
                    value={item[p]}
                    key={p}
                  />
                ))}
              </div>
            </List.Item>
          )}
        />
      </div>
    );
  }
}

const CurrencyList = injectIntl(CurrencyListComponent);

export default CurrencyList;
