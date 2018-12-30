import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Discount from './discount';
import DiscountDetail from './discount-detail';
import DiscountUpdate from './discount-update';
import DiscountDeleteDialog from './discount-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DiscountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DiscountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DiscountDetail} />
      <ErrorBoundaryRoute path={match.url} component={Discount} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DiscountDeleteDialog} />
  </>
);

export default Routes;
