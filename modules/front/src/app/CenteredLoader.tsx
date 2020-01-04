import { Icon } from "antd";
import Centered from "./common/Centered";
import * as React from "react";

export class CenteredLoader extends React.Component {
  render() {
    return (
      <Centered>
        <Icon type="loading" style={{ fontSize: 24 }} spin={true} />
      </Centered>
    );
  }
}
