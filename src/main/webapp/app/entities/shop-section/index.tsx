import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShopSection from './shop-section';
import ShopSectionDetail from './shop-section-detail';
import ShopSectionUpdate from './shop-section-update';
import ShopSectionDeleteDialog from './shop-section-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShopSectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShopSectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShopSectionDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShopSection} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ShopSectionDeleteDialog} />
  </>
);

export default Routes;
