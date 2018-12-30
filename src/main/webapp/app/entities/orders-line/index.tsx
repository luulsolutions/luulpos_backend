import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrdersLine from './orders-line';
import OrdersLineDetail from './orders-line-detail';
import OrdersLineUpdate from './orders-line-update';
import OrdersLineDeleteDialog from './orders-line-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrdersLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrdersLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrdersLineDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrdersLine} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OrdersLineDeleteDialog} />
  </>
);

export default Routes;
