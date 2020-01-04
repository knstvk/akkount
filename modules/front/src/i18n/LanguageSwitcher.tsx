import React, { CSSProperties } from "react";
import { Select } from "antd";
import { getMainStore } from "@cuba-platform/react";
import "./LanguageSwitcher.css";

export interface LanguageSwitcherProps {
  className?: string;
  style?: CSSProperties;
}

export class LanguageSwitcher extends React.Component<LanguageSwitcherProps> {
  handleChange = (locale: string) => {
    getMainStore().setLocale(locale);
  };

  render() {
    return (
      <Select
        defaultValue={getMainStore().locale}
        onChange={this.handleChange}
        size={"small"}
        style={this.props.style}
        className={this.props.className}
      >
        <Select.Option value="en">English</Select.Option>
        <Select.Option value="ru">Русский</Select.Option>
      </Select>
    );
  }
}
