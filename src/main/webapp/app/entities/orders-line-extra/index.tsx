import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrdersLineExtra from './orders-line-extra';
import OrdersLineExtraDetail from './orders-line-extra-detail';
import OrdersLineExtraUpdate from './orders-line-extra-update';
import OrdersLineExtraDeleteDialog from './orders-line-extra-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrdersLineExtraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrdersLineExtraUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrdersLineExtraDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrdersLineExtra} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OrdersLineExtraDeleteDialog} />
  </>
);

export default Routes;
