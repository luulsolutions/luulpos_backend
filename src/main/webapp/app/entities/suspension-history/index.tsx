import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SuspensionHistory from './suspension-history';
import SuspensionHistoryDetail from './suspension-history-detail';
import SuspensionHistoryUpdate from './suspension-history-update';
import SuspensionHistoryDeleteDialog from './suspension-history-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SuspensionHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SuspensionHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SuspensionHistoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={SuspensionHistory} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SuspensionHistoryDeleteDialog} />
  </>
);

export default Routes;
