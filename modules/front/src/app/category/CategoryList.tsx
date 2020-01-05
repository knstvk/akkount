import * as React from "react";

import {observer} from "mobx-react";

import {Button, Icon, List, Modal, Spin} from "antd";

import {Category} from "../../cuba/entities/akk$Category";
import {Link} from "react-router-dom";

import {collection, EntityProperty, injectMainStore, MainStoreInjected} from "@cuba-platform/react";

import {SerializedEntity} from "@cuba-platform/rest";
import {CategoryManagement} from "./CategoryManagement";
import {injectIntl, WrappedComponentProps} from "react-intl";

@injectMainStore
@observer
class CategoryListComponent extends React.Component<MainStoreInjected & WrappedComponentProps> {

  dataCollection = collection<Category>(Category.NAME, {
    view: "_local",
    sort: "name"
  });

  fields = ["name", "description", "catType"];

  showDeletionDialog = (e: SerializedEntity<Category>) => {
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
            to={CategoryManagement.PATH + "/" + CategoryManagement.NEW_SUBPATH}
          >
            <Button htmlType="button" type="primary" icon="plus">
              <span>
                Create Category
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
                <Link to={CategoryManagement.PATH + "/" + item.id} key="edit">
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
                    entityName={Category.NAME}
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

const CategoryList = injectIntl(CategoryListComponent);

export default CategoryList;
