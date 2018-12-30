import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SystemEventsHistory from './system-events-history';
import SystemEventsHistoryDetail from './system-events-history-detail';
import SystemEventsHistoryUpdate from './system-events-history-update';
import SystemEventsHistoryDeleteDialog from './system-events-history-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SystemEventsHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SystemEventsHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SystemEventsHistoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={SystemEventsHistory} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SystemEventsHistoryDeleteDialog} />
  </>
);

export default Routes;
