import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './email-balancer.reducer';
import { IEmailBalancer } from 'app/shared/model/email-balancer.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEmailBalancerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IEmailBalancerUpdateState {
  isNew: boolean;
}

export class EmailBalancerUpdate extends React.Component<IEmailBalancerUpdateProps, IEmailBalancerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { emailBalancerEntity } = this.props;
      const entity = {
        ...emailBalancerEntity,
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
    this.props.history.push('/entity/email-balancer');
  };

  render() {
    const { emailBalancerEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.emailBalancer.home.createOrEditLabel">
              <Translate contentKey="luulposApp.emailBalancer.home.createOrEditLabel">Create or edit a EmailBalancer</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : emailBalancerEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="email-balancer-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="relayIdLabel" for="relayId">
                    <Translate contentKey="luulposApp.emailBalancer.relayId">Relay Id</Translate>
                  </Label>
                  <AvField id="email-balancer-relayId" type="text" name="relayId" />
                </AvGroup>
                <AvGroup>
                  <Label id="relayPasswordLabel" for="relayPassword">
                    <Translate contentKey="luulposApp.emailBalancer.relayPassword">Relay Password</Translate>
                  </Label>
                  <AvField id="email-balancer-relayPassword" type="text" name="relayPassword" />
                </AvGroup>
                <AvGroup>
                  <Label id="startingCountLabel" for="startingCount">
                    <Translate contentKey="luulposApp.emailBalancer.startingCount">Starting Count</Translate>
                  </Label>
                  <AvField id="email-balancer-startingCount" type="string" className="form-control" name="startingCount" />
                </AvGroup>
                <AvGroup>
                  <Label id="endingCountLabel" for="endingCount">
                    <Translate contentKey="luulposApp.emailBalancer.endingCount">Ending Count</Translate>
                  </Label>
                  <AvField id="email-balancer-endingCount" type="string" className="form-control" name="endingCount" />
                </AvGroup>
                <AvGroup>
                  <Label id="providerLabel" for="provider">
                    <Translate contentKey="luulposApp.emailBalancer.provider">Provider</Translate>
                  </Label>
                  <AvField id="email-balancer-provider" type="text" name="provider" />
                </AvGroup>
                <AvGroup>
                  <Label id="relayPortLabel" for="relayPort">
                    <Translate contentKey="luulposApp.emailBalancer.relayPort">Relay Port</Translate>
                  </Label>
                  <AvField id="email-balancer-relayPort" type="string" className="form-control" name="relayPort" />
                </AvGroup>
                <AvGroup>
                  <Label id="enabledLabel" check>
                    <AvInput id="email-balancer-enabled" type="checkbox" className="form-control" name="enabled" />
                    <Translate contentKey="luulposApp.emailBalancer.enabled">Enabled</Translate>
                  </Label>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/email-balancer" replace color="info">
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
  emailBalancerEntity: storeState.emailBalancer.entity,
  loading: storeState.emailBalancer.loading,
  updating: storeState.emailBalancer.updating,
  updateSuccess: storeState.emailBalancer.updateSuccess
});

const mapDispatchToProps = {
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
)(EmailBalancerUpdate);
