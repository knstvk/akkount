import * as React from "react";
import { RouteComponentProps } from "react-router";
import { observer } from "mobx-react";
import CategoryEdit from "./CategoryEdit";
import CategoryList from "./CategoryList";

type Props = RouteComponentProps<{ entityId?: string }>;

@observer
export class CategoryManagement extends React.Component<Props> {
  static PATH = "/categoryManagement";
  static NEW_SUBPATH = "new";

  render() {
    const { entityId } = this.props.match.params;
    return (
      <>{entityId ? <CategoryEdit entityId={entityId} /> : <CategoryList />}</>
    );
  }
}
