import * as React from "react";
import { ChangeEvent, FormEvent } from "react";
import { Button, Form, Icon, Input, message } from "antd";
import { observer } from "mobx-react";
import { action, observable } from "mobx";
import { injectMainStore, MainStoreInjected } from "@cuba-platform/react";
import "./Login.css";
import logo from "./logo.png";
import { LanguageSwitcher } from "../../i18n/LanguageSwitcher";
import {
  FormattedMessage,
  injectIntl,
  WrappedComponentProps
} from "react-intl";

@injectMainStore
@observer
class Login extends React.Component<MainStoreInjected & WrappedComponentProps> {
  @observable login: string;
  @observable password: string;
  @observable performingLoginRequest = false;

  @action
  changeLogin = (e: ChangeEvent<HTMLInputElement>) => {
    this.login = e.target.value;
  };

  @action
  changePassword = (e: ChangeEvent<HTMLInputElement>) => {
    this.password = e.target.value;
  };

  @action
  doLogin = (e: FormEvent) => {
    e.preventDefault();
    this.performingLoginRequest = true;
    this.props
      .mainStore!.login(this.login, this.password)
      .then(
        action(() => {
          this.performingLoginRequest = false;
        })
      )
      .catch(
        action(() => {
          this.performingLoginRequest = false;
          message.error(this.props.intl.formatMessage({ id: "login.failed" }));
        })
      );
  };

  render() {
    return (
      <div className="login-form">
        <img
          src={logo}
          alt={this.props.intl.formatMessage({ id: "common.alt.logo" })}
          className="logo"
        />
        <div className="title">akkount</div>
        <Form layout="vertical" onSubmit={this.doLogin}>
          <Form.Item>
            <Input
              placeholder={this.props.intl.formatMessage({
                id: "login.placeholder.login"
              })}
              onChange={this.changeLogin}
              value={this.login}
              prefix={<Icon type="user" style={{ color: "rgba(0,0,0,.25)" }} />}
              size="large"
            />
          </Form.Item>
          <Form.Item>
            <Input
              placeholder={this.props.intl.formatMessage({
                id: "login.placeholder.password"
              })}
              onChange={this.changePassword}
              value={this.password}
              type="password"
              prefix={<Icon type="lock" style={{ color: "rgba(0,0,0,.25)" }} />}
              size="large"
            />
          </Form.Item>
          <Form.Item>
            <div style={{ float: "right" }}>
              <LanguageSwitcher className="language-switcher" />
            </div>
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              size="large"
              block={true}
              loading={this.performingLoginRequest}
            >
              <FormattedMessage id="login.loginBtn" />
            </Button>
          </Form.Item>
        </Form>
      </div>
    );
  }
}

export default injectIntl(Login);
