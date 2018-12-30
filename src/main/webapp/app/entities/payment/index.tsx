import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Payment from './payment';
import PaymentDetail from './payment-detail';
import PaymentUpdate from './payment-update';
import PaymentDeleteDialog from './payment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaymentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaymentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaymentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Payment} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PaymentDeleteDialog} />
  </>
);

export default Routes;
