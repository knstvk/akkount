import * as React from "react";
import {FormEvent} from "react";
import {Alert, Button, Card, Form, message} from "antd";
import {observer} from "mobx-react";
import {OperationManagement} from "./OperationManagement";
import {FormComponentProps} from "antd/lib/form";
import {Link, Redirect} from "react-router-dom";
import {IReactionDisposer, observable, reaction, toJS} from "mobx";
import {FormattedMessage, injectIntl, WrappedComponentProps} from "react-intl";

import {
  clearFieldErrors,
  collection,
  constructFieldsWithErrors,
  extractServerValidationErrors,
  Field,
  instance,
  MultilineText,
  withLocalizedForm
} from "@cuba-platform/react";

import "../../app/App.css";
import {Operation} from "../../cuba/entities/akk_Operation";

import {Account} from "../../cuba/entities/akk_Account";

import {Category} from "../../cuba/entities/akk_Category";
import Title from "antd/lib/typography/Title";
import {OperationType} from "../../cuba/enums/enums";

type Props = FormComponentProps & EditorProps;

type EditorProps = {
  entityId: string;
};

@observer
class OperationEditComponent extends React.Component<Props & WrappedComponentProps> {

  dataInstance = instance<Operation>(Operation.NAME, {
    view: "operation-edit",
    loadImmediately: false
  });

  acc1sDc = collection<Account>(Account.NAME, { view: "_minimal" });

  acc2sDc = collection<Account>(Account.NAME, { view: "_minimal" });

  categorysDc = collection<Category>(Category.NAME, { view: "_minimal" });

  @observable
  updated = false;
  reactionDisposer: IReactionDisposer;

  fields = [
    // "opType",
    "opDate",
    "acc1",
    "amount1",
    "acc2",
    "amount2",
    "category",
    "comments"
  ];

  @observable
  globalErrors: string[] = [];

  handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (err) {
        message.error(
          this.props.intl.formatMessage({
            id: "management.editor.validationError"
          })
        );
        return;
      }
      this.dataInstance
        .update(this.props.form.getFieldsValue(this.fields))
        .then(() => {
          message.success(
            this.props.intl.formatMessage({ id: "management.editor.success" })
          );
          this.updated = true;
        })
        .catch((e: any) => {
          if (e.response && typeof e.response.json === "function") {
            e.response.json().then((response: any) => {
              clearFieldErrors(this.props.form);
              const {
                globalErrors,
                fieldErrors
              } = extractServerValidationErrors(response);
              this.globalErrors = globalErrors;
              if (fieldErrors.size > 0) {
                this.props.form.setFields(
                  constructFieldsWithErrors(fieldErrors, this.props.form)
                );
              }

              if (fieldErrors.size > 0 || globalErrors.length > 0) {
                message.error(
                  this.props.intl.formatMessage({
                    id: "management.editor.validationError"
                  })
                );
              } else {
                message.error(
                  this.props.intl.formatMessage({
                    id: "management.editor.error"
                  })
                );
              }
            });
          } else {
            message.error(
              this.props.intl.formatMessage({ id: "management.editor.error" })
            );
          }
        });
    });
  };

  render() {
    if (this.updated) {
      return <Redirect to={OperationManagement.PATH} />;
    }

    const { status } = this.dataInstance;

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    };

    return (

      <Card className="narrow-layout">
        <Title level={4}>{this.dataInstance.item ? this.dataInstance.item.opType : "Loading..."}</Title>

        <Form onSubmit={this.handleSubmit} {...formItemLayout} >
          {/*<Field*/}
          {/*  entityName={Operation.NAME}*/}
          {/*  propertyName="opType"*/}
          {/*  form={this.props.form}*/}
          {/*  formItemOpts={{ style: { marginBottom: "12px" } }}*/}
          {/*  getFieldDecoratorOpts={{*/}
          {/*    rules: [{ required: true }]*/}
          {/*  }}*/}
          {/*/>*/}

          <Field
            entityName={Operation.NAME}
            propertyName="opDate"
            form={this.props.form}
            formItemOpts={{ style: { marginBottom: "12px" } }}
            getFieldDecoratorOpts={{
              rules: [{ required: true }]
            }}
          />

          {this.isExpense() ?
            <>
              <Field
                entityName={Operation.NAME}
                propertyName="acc1"
                form={this.props.form}
                formItemOpts={{ style: { marginBottom: "12px" } }}
                optionsContainer={this.acc1sDc}
                getFieldDecoratorOpts={{ rules: [{ required: true }] }}
              />

              <Field
                entityName={Operation.NAME}
                propertyName="amount1"
                form={this.props.form}
                formItemOpts={{ style: { marginBottom: "12px" } }}
                getFieldDecoratorOpts={{ rules: [{ required: true }] }}
              />
            </>
            : null
          }

          {this.isIncome() ?
            <>
              <Field
                entityName={Operation.NAME}
                propertyName="acc2"
                form={this.props.form}
                formItemOpts={{ style: { marginBottom: "12px" } }}
                optionsContainer={this.acc2sDc}
                getFieldDecoratorOpts={{ rules: [{ required: true }] }}
              />

              <Field
                entityName={Operation.NAME}
                propertyName="amount2"
                form={this.props.form}
                formItemOpts={{ style: { marginBottom: "12px" } }}
                getFieldDecoratorOpts={{ rules: [{ required: true }] }}
              />
            </>
            : null
          }

          {this.isNotTransfer() ?
            <Field
              entityName={Operation.NAME}
              propertyName="category"
              form={this.props.form}
              formItemOpts={{ style: { marginBottom: "12px" } }}
              optionsContainer={this.categorysDc}
              getFieldDecoratorOpts={{}}
            />
            : null
          }

          <Field
            entityName={Operation.NAME}
            propertyName="comments"
            form={this.props.form}
            formItemOpts={{ style: { marginBottom: "12px" } }}
            getFieldDecoratorOpts={{}}
          />

          {this.globalErrors.length > 0 && (
            <Alert
              message={<MultilineText lines={toJS(this.globalErrors)} />}
              type="error"
              style={{ marginBottom: "24px" }}
            />
          )}

          <Form.Item style={{ textAlign: "center" }}>
            <Link to={OperationManagement.PATH}>
              <Button htmlType="button">
                <FormattedMessage id="management.editor.cancel" />
              </Button>
            </Link>
            <Button
              type="primary"
              htmlType="submit"
              disabled={status !== "DONE" && status !== "ERROR"}
              loading={status === "LOADING"}
              style={{ marginLeft: "8px" }}
            >
              <FormattedMessage id="management.editor.submit" />
            </Button>
          </Form.Item>
        </Form>
      </Card>
    );
  }

  private isExpense() {
    return !this.dataInstance.item || (this.dataInstance.item.opType === OperationType.TRANSFER || this.dataInstance.item.opType === OperationType.EXPENSE);
  }

  private isIncome() {
    return !this.dataInstance.item || (this.dataInstance.item.opType === OperationType.TRANSFER || this.dataInstance.item.opType === OperationType.INCOME);
  }

  private isNotTransfer() {
    return !this.dataInstance.item || this.dataInstance.item.opType !== OperationType.TRANSFER;
  }

  componentDidMount() {
    if (!this.props.entityId.startsWith(OperationManagement.NEW_SUBPATH)) {
      this.dataInstance.load(this.props.entityId);
    } else {
      let operation = new Operation();
      operation.opDate = new Date();
      switch (this.props.entityId) {
        case OperationManagement.NEW_EXPENSE: operation.opType = OperationType.EXPENSE; break;
        case OperationManagement.NEW_INCOME: operation.opType = OperationType.INCOME; break;
        case OperationManagement.NEW_TRANSFER: operation.opType = OperationType.TRANSFER; break;
      }
      this.dataInstance.setItem(operation);

      // to set initial values into fields
      this.props.form.setFieldsValue(
        this.dataInstance.getFieldValues(this.fields)
      );
    }
    this.reactionDisposer = reaction(
      () => {
        return this.dataInstance.item;
      },
      () => {
        this.props.form.setFieldsValue(
          this.dataInstance.getFieldValues(this.fields)
        );
      }
    );
  }

  componentWillUnmount() {
    this.reactionDisposer();
  }
}

export default injectIntl(
  withLocalizedForm<EditorProps>({
    onValuesChange: (props: any, changedValues: any) => {
      // Reset server-side errors when field is edited
      Object.keys(changedValues).forEach((fieldName: string) => {
        props.form.setFields({
          [fieldName]: {
            value: changedValues[fieldName]
          }
        });
      });
    }
  })(OperationEditComponent)
);
