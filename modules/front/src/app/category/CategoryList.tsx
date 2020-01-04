import * as React from "react";

import {observable} from "mobx";

import {observer} from "mobx-react";

import {Button, Modal} from "antd";

import {Category} from "../../cuba/entities/akk$Category";
import {Link} from "react-router-dom";

import {collection, DataTable, injectMainStore, MainStoreInjected} from "@cuba-platform/react";

import {SerializedEntity} from "@cuba-platform/rest";
import {CategoryManagement} from "./CategoryManagement";
import {FormattedMessage, injectIntl, WrappedComponentProps} from "react-intl";

@injectMainStore
@observer
class CategoryListComponent extends React.Component<MainStoreInjected & WrappedComponentProps> {
  dataCollection = collection<Category>(Category.NAME, {
    view: "_local",
    sort: "-updateTs"
  });

  fields = ["name", "description", "catType"];

  @observable selectedRowKey: string | undefined;

  showDeletionDialog = (e: SerializedEntity<Category>) => {
    Modal.confirm({
      title: this.props.intl.formatMessage(
        {id: "management.browser.delete.areYouSure"},
        {instanceName: e._instanceName}
      ),
      okText: this.props.intl.formatMessage({
        id: "management.browser.delete.ok"
      }),
      cancelText: this.props.intl.formatMessage({
        id: "management.browser.delete.cancel"
      }),
      onOk: () => {
        this.selectedRowKey = undefined;

        return this.dataCollection.delete(e);
      }
    });
  };

  render() {
    const buttons = [
      <Link
        to={CategoryManagement.PATH + "/" + CategoryManagement.NEW_SUBPATH}
        key="create"
      >
        <Button
          htmlType="button"
          style={{margin: "0 12px 12px 0"}}
          type="primary"
          icon="plus"
        >
          <span>
            <FormattedMessage id="management.browser.create"/>
          </span>
        </Button>
      </Link>,
      <Link to={CategoryManagement.PATH + "/" + this.selectedRowKey} key="edit">
        <Button
          htmlType="button"
          style={{margin: "0 12px 12px 0"}}
          disabled={!this.selectedRowKey}
          type="default"
        >
          <FormattedMessage id="management.browser.edit"/>
        </Button>
      </Link>,
      <Button
        htmlType="button"
        style={{margin: "0 12px 12px 0"}}
        disabled={!this.selectedRowKey}
        onClick={this.deleteSelectedRow}
        key="remove"
        type="default"
      >
        <FormattedMessage id="management.browser.remove"/>
      </Button>
    ];

    return (
      <DataTable
        dataCollection={this.dataCollection}
        fields={this.fields}
        onRowSelectionChange={this.handleRowSelectionChange}
        hideSelectionColumn={true}
        buttons={buttons}
      />
    );
  }

  getRecordById(id: string): SerializedEntity<Category> {
    const record:
      | SerializedEntity<Category>
      | undefined = this.dataCollection.items.find(record => record.id === id);

    if (!record) {
      throw new Error("Cannot find entity with id " + id);
    }

    return record;
  }

  handleRowSelectionChange = (selectedRowKeys: string[]) => {
    this.selectedRowKey = selectedRowKeys[0];
  };

  deleteSelectedRow = () => {
    this.showDeletionDialog(this.getRecordById(this.selectedRowKey!));
  };
}

const CategoryList = injectIntl(CategoryListComponent);

export default CategoryList;
