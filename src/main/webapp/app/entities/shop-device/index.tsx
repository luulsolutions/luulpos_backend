import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShopDevice from './shop-device';
import ShopDeviceDetail from './shop-device-detail';
import ShopDeviceUpdate from './shop-device-update';
import ShopDeviceDeleteDialog from './shop-device-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopDeviceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopDeviceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopDeviceDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShopDevice} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ShopDeviceDeleteDialog} />
  </>
);

export default Routes;
