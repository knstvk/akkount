import * as React from "react";
import * as ReactDOM from "react-dom";
import App from "./App";
import { MainStore } from "@cuba-platform/react";
import { initializeApp } from "@cuba-platform/rest";
import { IntlProvider } from "react-intl";

it("renders without crashing", () => {
  const div = document.createElement("div");
  ReactDOM.render(
    <IntlProvider locale="en">
      <App mainStore={new MainStore(initializeApp())} />
    </IntlProvider>,
    div
  );
  ReactDOM.unmountComponentAtNode(div);
});
