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
import { getEntity, updateEntity, createEntity, reset } from './suspension-history.reducer';
import { ISuspensionHistory } from 'app/shared/model/suspension-history.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISuspensionHistoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISuspensionHistoryUpdateState {
  isNew: boolean;
  profileId: string;
  suspendedById: string;
}

export class SuspensionHistoryUpdate extends React.Component<ISuspensionHistoryUpdateProps, ISuspensionHistoryUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      profileId: '0',
      suspendedById: '0',
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
    values.suspendedDate = new Date(values.suspendedDate);
    values.unsuspensionDate = new Date(values.unsuspensionDate);

    if (errors.length === 0) {
      const { suspensionHistoryEntity } = this.props;
      const entity = {
        ...suspensionHistoryEntity,
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
    this.props.history.push('/entity/suspension-history');
  };

  render() {
    const { suspensionHistoryEntity, profiles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.suspensionHistory.home.createOrEditLabel">
              <Translate contentKey="luulposApp.suspensionHistory.home.createOrEditLabel">Create or edit a SuspensionHistory</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : suspensionHistoryEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="suspension-history-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="suspendedDateLabel" for="suspendedDate">
                    <Translate contentKey="luulposApp.suspensionHistory.suspendedDate">Suspended Date</Translate>
                  </Label>
                  <AvInput
                    id="suspension-history-suspendedDate"
                    type="datetime-local"
                    className="form-control"
                    name="suspendedDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.suspensionHistoryEntity.suspendedDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="suspensionTypeLabel">
                    <Translate contentKey="luulposApp.suspensionHistory.suspensionType">Suspension Type</Translate>
                  </Label>
                  <AvInput
                    id="suspension-history-suspensionType"
                    type="select"
                    className="form-control"
                    name="suspensionType"
                    value={(!isNew && suspensionHistoryEntity.suspensionType) || 'BANNED_FOR_LIFE'}
                  >
                    <option value="BANNED_FOR_LIFE">
                      <Translate contentKey="luulposApp.SuspensionType.BANNED_FOR_LIFE" />
                    </option>
                    <option value="BANNED_TEMPORARILY">
                      <Translate contentKey="luulposApp.SuspensionType.BANNED_TEMPORARILY" />
                    </option>
                    <option value="DELETED_BY_USER">
                      <Translate contentKey="luulposApp.SuspensionType.DELETED_BY_USER" />
                    </option>
                    <option value="DELETED_BY_ADMIN">
                      <Translate contentKey="luulposApp.SuspensionType.DELETED_BY_ADMIN" />
                    </option>
                    <option value="TEMP_DEACTIVATION_BY_USER">
                      <Translate contentKey="luulposApp.SuspensionType.TEMP_DEACTIVATION_BY_USER" />
                    </option>
                    <option value="UNDER_INVESTIGATION">
                      <Translate contentKey="luulposApp.SuspensionType.UNDER_INVESTIGATION" />
                    </option>
                    <option value="NONE">
                      <Translate contentKey="luulposApp.SuspensionType.NONE" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="suspendedReasonLabel" for="suspendedReason">
                    <Translate contentKey="luulposApp.suspensionHistory.suspendedReason">Suspended Reason</Translate>
                  </Label>
                  <AvField id="suspension-history-suspendedReason" type="text" name="suspendedReason" />
                </AvGroup>
                <AvGroup>
                  <Label id="resolutionNoteLabel" for="resolutionNote">
                    <Translate contentKey="luulposApp.suspensionHistory.resolutionNote">Resolution Note</Translate>
                  </Label>
                  <AvField id="suspension-history-resolutionNote" type="text" name="resolutionNote" />
                </AvGroup>
                <AvGroup>
                  <Label id="unsuspensionDateLabel" for="unsuspensionDate">
                    <Translate contentKey="luulposApp.suspensionHistory.unsuspensionDate">Unsuspension Date</Translate>
                  </Label>
                  <AvInput
                    id="suspension-history-unsuspensionDate"
                    type="datetime-local"
                    className="form-control"
                    name="unsuspensionDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.suspensionHistoryEntity.unsuspensionDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="profile.firstName">
                    <Translate contentKey="luulposApp.suspensionHistory.profile">Profile</Translate>
                  </Label>
                  <AvInput id="suspension-history-profile" type="select" className="form-control" name="profileId">
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
                  <Label for="suspendedBy.firstName">
                    <Translate contentKey="luulposApp.suspensionHistory.suspendedBy">Suspended By</Translate>
                  </Label>
                  <AvInput id="suspension-history-suspendedBy" type="select" className="form-control" name="suspendedById">
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
                <Button tag={Link} id="cancel-save" to="/entity/suspension-history" replace color="info">
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
  suspensionHistoryEntity: storeState.suspensionHistory.entity,
  loading: storeState.suspensionHistory.loading,
  updating: storeState.suspensionHistory.updating,
  updateSuccess: storeState.suspensionHistory.updateSuccess
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
)(SuspensionHistoryUpdate);
