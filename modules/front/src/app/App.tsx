import * as React from "react";
import "./App.css";

import {Layout} from "antd";
import {observer} from "mobx-react";
import Login from "./login/Login";
import Centered from "./common/Centered";
import AppHeader from "./header/AppHeader";
import {Route, Switch} from "react-router-dom";
import HomePage from "./home/HomePage";
import {menuItems} from "../routing";
import {injectMainStore, MainStoreInjected, RouteItem, SubMenu} from "@cuba-platform/react";
import {CenteredLoader} from "./CenteredLoader";
import {injectIntl, WrappedComponentProps} from "react-intl";

@injectMainStore
@observer
class AppComponent extends React.Component<
  MainStoreInjected & WrappedComponentProps
> {
  render() {
    const mainStore = this.props.mainStore!;
    const { initialized, locale, loginRequired } = mainStore;

    if (!initialized || !locale) {
      return <CenteredLoader />;
    }

    if (loginRequired) {
      return (
        <Centered>
          <Login />
        </Centered>
      );
    }

    return (
      <Layout className="main-layout">
        <Layout.Header style={{padding: 0}}>
          <AppHeader />
        </Layout.Header>
        <Layout>
          <Layout style={{ padding: "24px 24px 24px" }}>
            <Layout.Content>
              <Switch>
                <Route exact={true} path="/" component={HomePage} />
                {collectRouteItems(menuItems).map(route => (
                  <Route
                    key={route.pathPattern}
                    path={route.pathPattern}
                    component={route.component}
                  />
                ))}
              </Switch>
            </Layout.Content>
          </Layout>
        </Layout>
      </Layout>
    );
  }
}

function collectRouteItems(items: Array<RouteItem | SubMenu>): RouteItem[] {
  return items.reduce(
    (acc, curr) => {
      if ((curr as SubMenu).items == null) {
        // Route item
        acc.push(curr as RouteItem);
      } else {
        // Items from sub menu
        acc.push(...collectRouteItems((curr as SubMenu).items));
      }
      return acc;
    },
    [] as Array<RouteItem>
  );
}

const App = injectIntl(AppComponent);
export default App;
