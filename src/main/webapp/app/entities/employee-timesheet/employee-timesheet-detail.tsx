import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './employee-timesheet.reducer';
import { IEmployeeTimesheet } from 'app/shared/model/employee-timesheet.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmployeeTimesheetDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class EmployeeTimesheetDetail extends React.Component<IEmployeeTimesheetDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { employeeTimesheetEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.employeeTimesheet.detail.title">EmployeeTimesheet</Translate> [<b>
              {employeeTimesheetEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="checkinTime">
                <Translate contentKey="luulposApp.employeeTimesheet.checkinTime">Checkin Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={employeeTimesheetEntity.checkinTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="checkOutTime">
                <Translate contentKey="luulposApp.employeeTimesheet.checkOutTime">Check Out Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={employeeTimesheetEntity.checkOutTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="regularHoursWorked">
                <Translate contentKey="luulposApp.employeeTimesheet.regularHoursWorked">Regular Hours Worked</Translate>
              </span>
            </dt>
            <dd>{employeeTimesheetEntity.regularHoursWorked}</dd>
            <dt>
              <span id="overTimeHoursWorked">
                <Translate contentKey="luulposApp.employeeTimesheet.overTimeHoursWorked">Over Time Hours Worked</Translate>
              </span>
            </dt>
            <dd>{employeeTimesheetEntity.overTimeHoursWorked}</dd>
            <dt>
              <span id="pay">
                <Translate contentKey="luulposApp.employeeTimesheet.pay">Pay</Translate>
              </span>
            </dt>
            <dd>{employeeTimesheetEntity.pay}</dd>
            <dt>
              <Translate contentKey="luulposApp.employeeTimesheet.profile">Profile</Translate>
            </dt>
            <dd>{employeeTimesheetEntity.profileFirstName ? employeeTimesheetEntity.profileFirstName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.employeeTimesheet.shop">Shop</Translate>
            </dt>
            <dd>{employeeTimesheetEntity.shopShopName ? employeeTimesheetEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/employee-timesheet" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/employee-timesheet/${employeeTimesheetEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ employeeTimesheet }: IRootState) => ({
  employeeTimesheetEntity: employeeTimesheet.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EmployeeTimesheetDetail);
