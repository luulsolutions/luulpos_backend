import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShopChange from './shop-change';
import ShopChangeDetail from './shop-change-detail';
import ShopChangeUpdate from './shop-change-update';
import ShopChangeDeleteDialog from './shop-change-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopChangeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopChangeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopChangeDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShopChange} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ShopChangeDeleteDialog} />
  </>
);

export default Routes;
