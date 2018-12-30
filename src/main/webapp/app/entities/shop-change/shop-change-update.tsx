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
import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './shop-change.reducer';
import { IShopChange } from 'app/shared/model/shop-change.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShopChangeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IShopChangeUpdateState {
  isNew: boolean;
  shopId: string;
  changedById: string;
}

export class ShopChangeUpdate extends React.Component<IShopChangeUpdateProps, IShopChangeUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      shopId: '0',
      changedById: '0',
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
    this.props.getProfiles();
  }

  saveEntity = (event, errors, values) => {
    values.changeDate = new Date(values.changeDate);

    if (errors.length === 0) {
      const { shopChangeEntity } = this.props;
      const entity = {
        ...shopChangeEntity,
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
    this.props.history.push('/entity/shop-change');
  };

  render() {
    const { shopChangeEntity, shops, profiles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.shopChange.home.createOrEditLabel">
              <Translate contentKey="luulposApp.shopChange.home.createOrEditLabel">Create or edit a ShopChange</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : shopChangeEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="shop-change-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="changeLabel" for="change">
                    <Translate contentKey="luulposApp.shopChange.change">Change</Translate>
                  </Label>
                  <AvField id="shop-change-change" type="text" name="change" />
                </AvGroup>
                <AvGroup>
                  <Label id="changedEntityLabel" for="changedEntity">
                    <Translate contentKey="luulposApp.shopChange.changedEntity">Changed Entity</Translate>
                  </Label>
                  <AvField id="shop-change-changedEntity" type="text" name="changedEntity" />
                </AvGroup>
                <AvGroup>
                  <Label id="noteLabel" for="note">
                    <Translate contentKey="luulposApp.shopChange.note">Note</Translate>
                  </Label>
                  <AvField id="shop-change-note" type="text" name="note" />
                </AvGroup>
                <AvGroup>
                  <Label id="changeDateLabel" for="changeDate">
                    <Translate contentKey="luulposApp.shopChange.changeDate">Change Date</Translate>
                  </Label>
                  <AvInput
                    id="shop-change-changeDate"
                    type="datetime-local"
                    className="form-control"
                    name="changeDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.shopChangeEntity.changeDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.shopChange.shop">Shop</Translate>
                  </Label>
                  <AvInput id="shop-change-shop" type="select" className="form-control" name="shopId">
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
                <AvGroup>
                  <Label for="changedBy.firstName">
                    <Translate contentKey="luulposApp.shopChange.changedBy">Changed By</Translate>
                  </Label>
                  <AvInput id="shop-change-changedBy" type="select" className="form-control" name="changedById">
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
                <Button tag={Link} id="cancel-save" to="/entity/shop-change" replace color="info">
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
  profiles: storeState.profile.entities,
  shopChangeEntity: storeState.shopChange.entity,
  loading: storeState.shopChange.loading,
  updating: storeState.shopChange.updating,
  updateSuccess: storeState.shopChange.updateSuccess
});

const mapDispatchToProps = {
  getShops,
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
)(ShopChangeUpdate);
