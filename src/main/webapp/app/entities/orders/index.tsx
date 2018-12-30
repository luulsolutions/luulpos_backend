import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Orders from './orders';
import OrdersDetail from './orders-detail';
import OrdersUpdate from './orders-update';
import OrdersDeleteDialog from './orders-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrdersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrdersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrdersDetail} />
      <ErrorBoundaryRoute path={match.url} component={Orders} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OrdersDeleteDialog} />
  </>
);

export default Routes;
