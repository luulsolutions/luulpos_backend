import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { getEntities as getPaymentMethods } from 'app/entities/payment-method/payment-method.reducer';
import { getEntity, updateEntity, createEntity, reset } from './payment-method-config.reducer';
import { IPaymentMethodConfig } from 'app/shared/model/payment-method-config.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPaymentMethodConfigUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPaymentMethodConfigUpdateState {
  isNew: boolean;
  paymentMethodId: string;
}

export class PaymentMethodConfigUpdate extends React.Component<IPaymentMethodConfigUpdateProps, IPaymentMethodConfigUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      paymentMethodId: '0',
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

    this.props.getPaymentMethods();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { paymentMethodConfigEntity } = this.props;
      const entity = {
        ...paymentMethodConfigEntity,
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
    this.props.history.push('/entity/payment-method-config');
  };

  render() {
    const { paymentMethodConfigEntity, paymentMethods, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.paymentMethodConfig.home.createOrEditLabel">
              <Translate contentKey="luulposApp.paymentMethodConfig.home.createOrEditLabel">Create or edit a PaymentMethodConfig</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : paymentMethodConfigEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="payment-method-config-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="keyLabel" for="key">
                    <Translate contentKey="luulposApp.paymentMethodConfig.key">Key</Translate>
                  </Label>
                  <AvField id="payment-method-config-key" type="text" name="key" />
                </AvGroup>
                <AvGroup>
                  <Label id="valueLabel" for="value">
                    <Translate contentKey="luulposApp.paymentMethodConfig.value">Value</Translate>
                  </Label>
                  <AvField id="payment-method-config-value" type="text" name="value" />
                </AvGroup>
                <AvGroup>
                  <Label id="noteLabel" for="note">
                    <Translate contentKey="luulposApp.paymentMethodConfig.note">Note</Translate>
                  </Label>
                  <AvField id="payment-method-config-note" type="text" name="note" />
                </AvGroup>
                <AvGroup>
                  <Label id="enabledLabel" check>
                    <AvInput id="payment-method-config-enabled" type="checkbox" className="form-control" name="enabled" />
                    <Translate contentKey="luulposApp.paymentMethodConfig.enabled">Enabled</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="paymentMethod.paymentMethod">
                    <Translate contentKey="luulposApp.paymentMethodConfig.paymentMethod">Payment Method</Translate>
                  </Label>
                  <AvInput id="payment-method-config-paymentMethod" type="select" className="form-control" name="paymentMethodId">
                    <option value="" key="0" />
                    {paymentMethods
                      ? paymentMethods.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.paymentMethod}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/payment-method-config" replace color="info">
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
  paymentMethods: storeState.paymentMethod.entities,
  paymentMethodConfigEntity: storeState.paymentMethodConfig.entity,
  loading: storeState.paymentMethodConfig.loading,
  updating: storeState.paymentMethodConfig.updating,
  updateSuccess: storeState.paymentMethodConfig.updateSuccess
});

const mapDispatchToProps = {
  getPaymentMethods,
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
)(PaymentMethodConfigUpdate);
