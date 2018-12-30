import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import EmployeeTimesheet from './employee-timesheet';
import EmployeeTimesheetDetail from './employee-timesheet-detail';
import EmployeeTimesheetUpdate from './employee-timesheet-update';
import EmployeeTimesheetDeleteDialog from './employee-timesheet-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmployeeTimesheetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmployeeTimesheetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmployeeTimesheetDetail} />
      <ErrorBoundaryRoute path={match.url} component={EmployeeTimesheet} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EmployeeTimesheetDeleteDialog} />
  </>
);

export default Routes;
