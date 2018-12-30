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
import { getEntity, updateEntity, createEntity, reset } from './system-events-history.reducer';
import { ISystemEventsHistory } from 'app/shared/model/system-events-history.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISystemEventsHistoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISystemEventsHistoryUpdateState {
  isNew: boolean;
  triggedById: string;
}

export class SystemEventsHistoryUpdate extends React.Component<ISystemEventsHistoryUpdateProps, ISystemEventsHistoryUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      triggedById: '0',
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
  }

  saveEntity = (event, errors, values) => {
    values.eventDate = new Date(values.eventDate);

    if (errors.length === 0) {
      const { systemEventsHistoryEntity } = this.props;
      const entity = {
        ...systemEventsHistoryEntity,
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
    this.props.history.push('/entity/system-events-history');
  };

  render() {
    const { systemEventsHistoryEntity, profiles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.systemEventsHistory.home.createOrEditLabel">
              <Translate contentKey="luulposApp.systemEventsHistory.home.createOrEditLabel">Create or edit a SystemEventsHistory</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : systemEventsHistoryEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="system-events-history-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="eventNameLabel" for="eventName">
                    <Translate contentKey="luulposApp.systemEventsHistory.eventName">Event Name</Translate>
                  </Label>
                  <AvField
                    id="system-events-history-eventName"
                    type="text"
                    name="eventName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="eventDateLabel" for="eventDate">
                    <Translate contentKey="luulposApp.systemEventsHistory.eventDate">Event Date</Translate>
                  </Label>
                  <AvInput
                    id="system-events-history-eventDate"
                    type="datetime-local"
                    className="form-control"
                    name="eventDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.systemEventsHistoryEntity.eventDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="eventApiLabel" for="eventApi">
                    <Translate contentKey="luulposApp.systemEventsHistory.eventApi">Event Api</Translate>
                  </Label>
                  <AvField id="system-events-history-eventApi" type="text" name="eventApi" />
                </AvGroup>
                <AvGroup>
                  <Label id="eventNoteLabel" for="eventNote">
                    <Translate contentKey="luulposApp.systemEventsHistory.eventNote">Event Note</Translate>
                  </Label>
                  <AvField id="system-events-history-eventNote" type="text" name="eventNote" />
                </AvGroup>
                <AvGroup>
                  <Label id="eventEntityNameLabel" for="eventEntityName">
                    <Translate contentKey="luulposApp.systemEventsHistory.eventEntityName">Event Entity Name</Translate>
                  </Label>
                  <AvField id="system-events-history-eventEntityName" type="text" name="eventEntityName" />
                </AvGroup>
                <AvGroup>
                  <Label id="eventEntityIdLabel" for="eventEntityId">
                    <Translate contentKey="luulposApp.systemEventsHistory.eventEntityId">Event Entity Id</Translate>
                  </Label>
                  <AvField id="system-events-history-eventEntityId" type="string" className="form-control" name="eventEntityId" />
                </AvGroup>
                <AvGroup>
                  <Label for="triggedBy.firstName">
                    <Translate contentKey="luulposApp.systemEventsHistory.triggedBy">Trigged By</Translate>
                  </Label>
                  <AvInput id="system-events-history-triggedBy" type="select" className="form-control" name="triggedById">
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
                <Button tag={Link} id="cancel-save" to="/entity/system-events-history" replace color="info">
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
  systemEventsHistoryEntity: storeState.systemEventsHistory.entity,
  loading: storeState.systemEventsHistory.loading,
  updating: storeState.systemEventsHistory.updating,
  updateSuccess: storeState.systemEventsHistory.updateSuccess
});

const mapDispatchToProps = {
  getProfiles,
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
)(SystemEventsHistoryUpdate);
