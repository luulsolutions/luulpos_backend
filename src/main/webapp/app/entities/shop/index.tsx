import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Shop from './shop';
import ShopDetail from './shop-detail';
import ShopUpdate from './shop-update';
import ShopDeleteDialog from './shop-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopDetail} />
      <ErrorBoundaryRoute path={match.url} component={Shop} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ShopDeleteDialog} />
  </>
);

export default Routes;
