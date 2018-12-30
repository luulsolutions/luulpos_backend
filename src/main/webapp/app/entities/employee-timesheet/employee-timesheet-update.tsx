import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { getEntity, updateEntity, createEntity, reset } from './employee-timesheet.reducer';
import { IEmployeeTimesheet } from 'app/shared/model/employee-timesheet.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEmployeeTimesheetUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IEmployeeTimesheetUpdateState {
  isNew: boolean;
  profileId: string;
  shopId: string;
}

export class EmployeeTimesheetUpdate extends React.Component<IEmployeeTimesheetUpdateProps, IEmployeeTimesheetUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      profileId: '0',
      shopId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getProfiles();
    this.props.getShops();
  }

  saveEntity = (event, errors, values) => {
    values.checkinTime = new Date(values.checkinTime);
    values.checkOutTime = new Date(values.checkOutTime);

    if (errors.length === 0) {
      const { employeeTimesheetEntity } = this.props;
      const entity = {
        ...employeeTimesheetEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/employee-timesheet');
  };

  render() {
    const { employeeTimesheetEntity, profiles, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.employeeTimesheet.home.createOrEditLabel">
              <Translate contentKey="luulposApp.employeeTimesheet.home.createOrEditLabel">Create or edit a EmployeeTimesheet</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : employeeTimesheetEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="employee-timesheet-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="checkinTimeLabel" for="checkinTime">
                    <Translate contentKey="luulposApp.employeeTimesheet.checkinTime">Checkin Time</Translate>
                  </Label>
                  <AvInput
                    id="employee-timesheet-checkinTime"
                    type="datetime-local"
                    className="form-control"
                    name="checkinTime"
                    value={isNew ? null : convertDateTimeFromServer(this.props.employeeTimesheetEntity.checkinTime)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="checkOutTimeLabel" for="checkOutTime">
                    <Translate contentKey="luulposApp.employeeTimesheet.checkOutTime">Check Out Time</Translate>
                  </Label>
                  <AvInput
                    id="employee-timesheet-checkOutTime"
                    type="datetime-local"
                    className="form-control"
                    name="checkOutTime"
                    value={isNew ? null : convertDateTimeFromServer(this.props.employeeTimesheetEntity.checkOutTime)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="regularHoursWorkedLabel" for="regularHoursWorked">
                    <Translate contentKey="luulposApp.employeeTimesheet.regularHoursWorked">Regular Hours Worked</Translate>
                  </Label>
                  <AvField id="employee-timesheet-regularHoursWorked" type="string" className="form-control" name="regularHoursWorked" />
                </AvGroup>
                <AvGroup>
                  <Label id="overTimeHoursWorkedLabel" for="overTimeHoursWorked">
                    <Translate contentKey="luulposApp.employeeTimesheet.overTimeHoursWorked">Over Time Hours Worked</Translate>
                  </Label>
                  <AvField id="employee-timesheet-overTimeHoursWorked" type="string" className="form-control" name="overTimeHoursWorked" />
                </AvGroup>
                <AvGroup>
                  <Label id="payLabel" for="pay">
                    <Translate contentKey="luulposApp.employeeTimesheet.pay">Pay</Translate>
                  </Label>
                  <AvField id="employee-timesheet-pay" type="text" name="pay" />
                </AvGroup>
                <AvGroup>
                  <Label for="profile.firstName">
                    <Translate contentKey="luulposApp.employeeTimesheet.profile">Profile</Translate>
                  </Label>
                  <AvInput id="employee-timesheet-profile" type="select" className="form-control" name="profileId">
                    <option value="" key="0" />
                    {profiles
                      ? profiles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.firstName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.employeeTimesheet.shop">Shop</Translate>
                  </Label>
                  <AvInput id="employee-timesheet-shop" type="select" className="form-control" name="shopId">
                    <option value="" key="0" />
                    {shops
                      ? shops.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.shopName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/employee-timesheet" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  profiles: storeState.profile.entities,
  shops: storeState.shop.entities,
  employeeTimesheetEntity: storeState.employeeTimesheet.entity,
  loading: storeState.employeeTimesheet.loading,
  updating: storeState.employeeTimesheet.updating,
  updateSuccess: storeState.employeeTimesheet.updateSuccess
});

const mapDispatchToProps = {
  getProfiles,
  getShops,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EmployeeTimesheetUpdate);
