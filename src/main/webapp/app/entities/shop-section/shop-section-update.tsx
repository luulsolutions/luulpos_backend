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
import { getEntity, updateEntity, createEntity, reset } from './shop-section.reducer';
import { IShopSection } from 'app/shared/model/shop-section.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShopSectionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IShopSectionUpdateState {
  isNew: boolean;
  shopId: string;
}

export class ShopSectionUpdate extends React.Component<IShopSectionUpdateProps, IShopSectionUpdateState> {
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
      const { shopSectionEntity } = this.props;
      const entity = {
        ...shopSectionEntity,
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
    this.props.history.push('/entity/shop-section');
  };

  render() {
    const { shopSectionEntity, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.shopSection.home.createOrEditLabel">
              <Translate contentKey="luulposApp.shopSection.home.createOrEditLabel">Create or edit a ShopSection</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : shopSectionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="shop-section-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="sectionNameLabel" for="sectionName">
                    <Translate contentKey="luulposApp.shopSection.sectionName">Section Name</Translate>
                  </Label>
                  <AvField id="shop-section-sectionName" type="text" name="sectionName" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.shopSection.description">Description</Translate>
                  </Label>
                  <AvField id="shop-section-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="surchargePercentageLabel" for="surchargePercentage">
                    <Translate contentKey="luulposApp.shopSection.surchargePercentage">Surcharge Percentage</Translate>
                  </Label>
                  <AvField id="shop-section-surchargePercentage" type="string" className="form-control" name="surchargePercentage" />
                </AvGroup>
                <AvGroup>
                  <Label id="surchargeFlatAmountLabel" for="surchargeFlatAmount">
                    <Translate contentKey="luulposApp.shopSection.surchargeFlatAmount">Surcharge Flat Amount</Translate>
                  </Label>
                  <AvField id="shop-section-surchargeFlatAmount" type="text" name="surchargeFlatAmount" />
                </AvGroup>
                <AvGroup>
                  <Label id="usePercentageLabel" check>
                    <AvInput id="shop-section-usePercentage" type="checkbox" className="form-control" name="usePercentage" />
                    <Translate contentKey="luulposApp.shopSection.usePercentage">Use Percentage</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.shopSection.shop">Shop</Translate>
                  </Label>
                  <AvInput id="shop-section-shop" type="select" className="form-control" name="shopId">
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
                <Button tag={Link} id="cancel-save" to="/entity/shop-section" replace color="info">
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
  shopSectionEntity: storeState.shopSection.entity,
  loading: storeState.shopSection.loading,
  updating: storeState.shopSection.updating,
  updateSuccess: storeState.shopSection.updateSuccess
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
)(ShopSectionUpdate);
