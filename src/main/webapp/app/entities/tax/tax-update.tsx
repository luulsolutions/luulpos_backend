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
import { getEntity, updateEntity, createEntity, reset } from './tax.reducer';
import { ITax } from 'app/shared/model/tax.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITaxUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITaxUpdateState {
  isNew: boolean;
  shopId: string;
}

export class TaxUpdate extends React.Component<ITaxUpdateProps, ITaxUpdateState> {
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
      const { taxEntity } = this.props;
      const entity = {
        ...taxEntity,
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
    this.props.history.push('/entity/tax');
  };

  render() {
    const { taxEntity, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.tax.home.createOrEditLabel">
              <Translate contentKey="luulposApp.tax.home.createOrEditLabel">Create or edit a Tax</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : taxEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="tax-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.tax.description">Description</Translate>
                  </Label>
                  <AvField
                    id="tax-description"
                    type="text"
                    name="description"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="percentageLabel" for="percentage">
                    <Translate contentKey="luulposApp.tax.percentage">Percentage</Translate>
                  </Label>
                  <AvField
                    id="tax-percentage"
                    type="string"
                    className="form-control"
                    name="percentage"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="amountLabel" for="amount">
                    <Translate contentKey="luulposApp.tax.amount">Amount</Translate>
                  </Label>
                  <AvField
                    id="tax-amount"
                    type="text"
                    name="amount"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="tax-active" type="checkbox" className="form-control" name="active" />
                    <Translate contentKey="luulposApp.tax.active">Active</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.tax.shop">Shop</Translate>
                  </Label>
                  <AvInput id="tax-shop" type="select" className="form-control" name="shopId">
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
                <Button tag={Link} id="cancel-save" to="/entity/tax" replace color="info">
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
  taxEntity: storeState.tax.entity,
  loading: storeState.tax.loading,
  updating: storeState.tax.updating,
  updateSuccess: storeState.tax.updateSuccess
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
)(TaxUpdate);
