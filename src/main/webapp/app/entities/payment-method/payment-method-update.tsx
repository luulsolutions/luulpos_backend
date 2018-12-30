import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { getEntity, updateEntity, createEntity, reset } from './payment-method.reducer';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPaymentMethodUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPaymentMethodUpdateState {
  isNew: boolean;
  shopId: string;
}

export class PaymentMethodUpdate extends React.Component<IPaymentMethodUpdateProps, IPaymentMethodUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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

    this.props.getShops();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { paymentMethodEntity } = this.props;
      const entity = {
        ...paymentMethodEntity,
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
    this.props.history.push('/entity/payment-method');
  };

  render() {
    const { paymentMethodEntity, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.paymentMethod.home.createOrEditLabel">
              <Translate contentKey="luulposApp.paymentMethod.home.createOrEditLabel">Create or edit a PaymentMethod</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : paymentMethodEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="payment-method-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="paymentMethodLabel" for="paymentMethod">
                    <Translate contentKey="luulposApp.paymentMethod.paymentMethod">Payment Method</Translate>
                  </Label>
                  <AvField
                    id="payment-method-paymentMethod"
                    type="text"
                    name="paymentMethod"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.paymentMethod.description">Description</Translate>
                  </Label>
                  <AvField id="payment-method-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="payment-method-active" type="checkbox" className="form-control" name="active" />
                    <Translate contentKey="luulposApp.paymentMethod.active">Active</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.paymentMethod.shop">Shop</Translate>
                  </Label>
                  <AvInput id="payment-method-shop" type="select" className="form-control" name="shopId">
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
                <Button tag={Link} id="cancel-save" to="/entity/payment-method" replace color="info">
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
  shops: storeState.shop.entities,
  paymentMethodEntity: storeState.paymentMethod.entity,
  loading: storeState.paymentMethod.loading,
  updating: storeState.paymentMethod.updating,
  updateSuccess: storeState.paymentMethod.updateSuccess
});

const mapDispatchToProps = {
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
)(PaymentMethodUpdate);
