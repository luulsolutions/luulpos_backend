import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SystemConfig from './system-config';
import SystemConfigDetail from './system-config-detail';
import SystemConfigUpdate from './system-config-update';
import SystemConfigDeleteDialog from './system-config-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SystemConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SystemConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SystemConfigDetail} />
      <ErrorBoundaryRoute path={match.url} component={SystemConfig} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SystemConfigDeleteDialog} />
  </>
);

export default Routes;
