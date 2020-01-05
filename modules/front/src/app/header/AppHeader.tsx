import {Button, Icon, Menu, Modal} from "antd";
import * as React from "react";
import {observer} from "mobx-react";
import "./AppHeader.css";
import {injectMainStore, MainStoreInjected} from "@cuba-platform/react";
import {FormattedMessage, injectIntl, WrappedComponentProps} from "react-intl";
import {NavLink} from "react-router-dom";

@injectMainStore
@observer
class AppHeader extends React.Component<MainStoreInjected & WrappedComponentProps> {

  render() {
    return (
      <div className="app-header">
        <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={['1']}
          style={{ lineHeight: '64px' }}
        >
          <Menu.Item key="1">
            <NavLink to={"/"}>
              {/*<Icon type="home" />*/}
              <FormattedMessage id="router.home" />
            </NavLink>
          </Menu.Item>
          <Menu.Item key="2">
            <NavLink to={"/operationManagement"}>
              <FormattedMessage id="router.OperationManagement" />
            </NavLink>
          </Menu.Item>
          <Menu.SubMenu
            title={ <Icon type="setting" /> }
          >
            <Menu.Item key="3">
              <NavLink to={"/accountManagement"}>
                <FormattedMessage id="router.AccountManagement" />
              </NavLink>
            </Menu.Item>
            <Menu.Item key="4">
              <NavLink to={"/categoryManagement"}>
                <FormattedMessage id="router.CategoryManagement" />
              </NavLink>
            </Menu.Item>
            <Menu.Item key="5">
              <NavLink to={"/currencyManagement"}>
                <FormattedMessage id="router.CurrencyManagement" />
              </NavLink>
            </Menu.Item>
          </Menu.SubMenu>
        </Menu>
        {/*<div>*/}
        {/*  <img*/}
        {/*    src={logo}*/}
        {/*    alt={this.props.intl.formatMessage({ id: "common.alt.logo" })}*/}
        {/*  />*/}
        {/*</div>*/}
        <div className="user-panel">
          {/*<LanguageSwitcher className="panelelement language-switcher -header" />*/}
          {/*<span className="panelelement">{appState.userName}</span>*/}
          <Button
            className="panelelement"
            ghost={true}
            icon="logout"
            style={{ border: 0 }}
            onClick={this.showLogoutConfirm}
          />
        </div>
      </div>
    );
  }

  showLogoutConfirm = () => {
    Modal.confirm({
      title: this.props.intl.formatMessage({ id: "header.logout.areYouSure" }),
      okText: this.props.intl.formatMessage({ id: "header.logout.ok" }),
      cancelText: this.props.intl.formatMessage({ id: "header.logout.cancel" }),
      onOk: () => {
        this.props.mainStore!.logout();
      }
    });
  };
}

export default injectIntl(AppHeader);
