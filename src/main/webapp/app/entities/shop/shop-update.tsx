import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './shop.reducer';
import { IShop } from 'app/shared/model/shop.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShopUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IShopUpdateState {
  isNew: boolean;
  companyId: string;
  approvedById: string;
  profilesId: string;
}

export class ShopUpdate extends React.Component<IShopUpdateProps, IShopUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      companyId: '0',
      approvedById: '0',
      profilesId: '0',
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

    this.props.getCompanies();
    this.props.getProfiles();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.approvalDate = new Date(values.approvalDate);
    values.createdDate = new Date(values.createdDate);

    if (errors.length === 0) {
      const { shopEntity } = this.props;
      const entity = {
        ...shopEntity,
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
    this.props.history.push('/entity/shop');
  };

  render() {
    const { shopEntity, companies, profiles, loading, updating } = this.props;
    const { isNew } = this.state;

    const { shopLogo, shopLogoContentType } = shopEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.shop.home.createOrEditLabel">
              <Translate contentKey="luulposApp.shop.home.createOrEditLabel">Create or edit a Shop</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : shopEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="shop-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="shopNameLabel" for="shopName">
                    <Translate contentKey="luulposApp.shop.shopName">Shop Name</Translate>
                  </Label>
                  <AvField
                    id="shop-shopName"
                    type="text"
                    name="shopName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="shopAccountTypeLabel">
                    <Translate contentKey="luulposApp.shop.shopAccountType">Shop Account Type</Translate>
                  </Label>
                  <AvInput
                    id="shop-shopAccountType"
                    type="select"
                    className="form-control"
                    name="shopAccountType"
                    value={(!isNew && shopEntity.shopAccountType) || 'TRIAL_ACCOUNT'}
                  >
                    <option value="TRIAL_ACCOUNT">
                      <Translate contentKey="luulposApp.ShopAccountType.TRIAL_ACCOUNT" />
                    </option>
                    <option value="SILVER_ACCOUNT">
                      <Translate contentKey="luulposApp.ShopAccountType.SILVER_ACCOUNT" />
                    </option>
                    <option value="GOLD_ACCOUNT">
                      <Translate contentKey="luulposApp.ShopAccountType.GOLD_ACCOUNT" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="approvalDateLabel" for="approvalDate">
                    <Translate contentKey="luulposApp.shop.approvalDate">Approval Date</Translate>
                  </Label>
                  <AvInput
                    id="shop-approvalDate"
                    type="datetime-local"
                    className="form-control"
                    name="approvalDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.shopEntity.approvalDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="address">
                    <Translate contentKey="luulposApp.shop.address">Address</Translate>
                  </Label>
                  <AvField id="shop-address" type="text" name="address" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="luulposApp.shop.email">Email</Translate>
                  </Label>
                  <AvField id="shop-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="luulposApp.shop.description">Description</Translate>
                  </Label>
                  <AvField id="shop-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="noteLabel" for="note">
                    <Translate contentKey="luulposApp.shop.note">Note</Translate>
                  </Label>
                  <AvField id="shop-note" type="text" name="note" />
                </AvGroup>
                <AvGroup>
                  <Label id="landlandLabel" for="landland">
                    <Translate contentKey="luulposApp.shop.landland">Landland</Translate>
                  </Label>
                  <AvField id="shop-landland" type="text" name="landland" />
                </AvGroup>
                <AvGroup>
                  <Label id="mobileLabel" for="mobile">
                    <Translate contentKey="luulposApp.shop.mobile">Mobile</Translate>
                  </Label>
                  <AvField id="shop-mobile" type="text" name="mobile" />
                </AvGroup>
                <AvGroup>
                  <Label id="createdDateLabel" for="createdDate">
                    <Translate contentKey="luulposApp.shop.createdDate">Created Date</Translate>
                  </Label>
                  <AvInput
                    id="shop-createdDate"
                    type="datetime-local"
                    className="form-control"
                    name="createdDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.shopEntity.createdDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="shopLogoLabel" for="shopLogo">
                      <Translate contentKey="luulposApp.shop.shopLogo">Shop Logo</Translate>
                    </Label>
                    <br />
                    {shopLogo ? (
                      <div>
                        <a onClick={openFile(shopLogoContentType, shopLogo)}>
                          <img src={`data:${shopLogoContentType};base64,${shopLogo}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {shopLogoContentType}, {byteSize(shopLogo)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('shopLogo')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_shopLogo" type="file" onChange={this.onBlobChange(true, 'shopLogo')} accept="image/*" />
                    <AvInput type="hidden" name="shopLogo" value={shopLogo} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="shopLogoUrlLabel" for="shopLogoUrl">
                    <Translate contentKey="luulposApp.shop.shopLogoUrl">Shop Logo Url</Translate>
                  </Label>
                  <AvField id="shop-shopLogoUrl" type="text" name="shopLogoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="shop-active" type="checkbox" className="form-control" name="active" />
                    <Translate contentKey="luulposApp.shop.active">Active</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="currencyLabel" for="currency">
                    <Translate contentKey="luulposApp.shop.currency">Currency</Translate>
                  </Label>
                  <AvField id="shop-currency" type="text" name="currency" />
                </AvGroup>
                <AvGroup>
                  <Label for="company.companyName">
                    <Translate contentKey="luulposApp.shop.company">Company</Translate>
                  </Label>
                  <AvInput id="shop-company" type="select" className="form-control" name="companyId">
                    <option value="" key="0" />
                    {companies
                      ? companies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.companyName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="approvedBy.firstName">
                    <Translate contentKey="luulposApp.shop.approvedBy">Approved By</Translate>
                  </Label>
                  <AvInput id="shop-approvedBy" type="select" className="form-control" name="approvedById">
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
                <Button tag={Link} id="cancel-save" to="/entity/shop" replace color="info">
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
  companies: storeState.company.entities,
  profiles: storeState.profile.entities,
  shopEntity: storeState.shop.entity,
  loading: storeState.shop.loading,
  updating: storeState.shop.updating,
  updateSuccess: storeState.shop.updateSuccess
});

const mapDispatchToProps = {
  getCompanies,
  getProfiles,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShopUpdate);
