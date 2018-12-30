import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EmailBalancer from './email-balancer';
import EmailBalancerDetail from './email-balancer-detail';
import EmailBalancerUpdate from './email-balancer-update';
import EmailBalancerDeleteDialog from './email-balancer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmailBalancerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmailBalancerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmailBalancerDetail} />
      <ErrorBoundaryRoute path={match.url} component={EmailBalancer} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EmailBalancerDeleteDialog} />
  </>
);

export default Routes;
