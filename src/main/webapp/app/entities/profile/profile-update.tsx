import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IShop } from 'app/shared/model/shop.model';
import { getEntities as getShops } from 'app/entities/shop/shop.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IProfileUpdateState {
  isNew: boolean;
  userId: string;
  shopId: string;
}

export class ProfileUpdate extends React.Component<IProfileUpdateProps, IProfileUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
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

    this.props.getUsers();
    this.props.getShops();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.registerationDate = new Date(values.registerationDate);
    values.lastAccess = new Date(values.lastAccess);

    if (errors.length === 0) {
      const { profileEntity } = this.props;
      const entity = {
        ...profileEntity,
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
    this.props.history.push('/entity/profile');
  };

  render() {
    const { profileEntity, users, shops, loading, updating } = this.props;
    const { isNew } = this.state;

    const { thumbnailPhoto, thumbnailPhotoContentType, fullPhoto, fullPhotoContentType } = profileEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="luulposApp.profile.home.createOrEditLabel">
              <Translate contentKey="luulposApp.profile.home.createOrEditLabel">Create or edit a Profile</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : profileEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="profile-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="firstNameLabel" for="firstName">
                    <Translate contentKey="luulposApp.profile.firstName">First Name</Translate>
                  </Label>
                  <AvField
                    id="profile-firstName"
                    type="text"
                    name="firstName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="lastName">
                    <Translate contentKey="luulposApp.profile.lastName">Last Name</Translate>
                  </Label>
                  <AvField
                    id="profile-lastName"
                    type="text"
                    name="lastName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="luulposApp.profile.email">Email</Translate>
                  </Label>
                  <AvField
                    id="profile-email"
                    type="text"
                    name="email"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="userTypeLabel">
                    <Translate contentKey="luulposApp.profile.userType">User Type</Translate>
                  </Label>
                  <AvInput
                    id="profile-userType"
                    type="select"
                    className="form-control"
                    name="userType"
                    value={(!isNew && profileEntity.userType) || 'SYSTEM_MANAGER'}
                  >
                    <option value="SYSTEM_MANAGER">
                      <Translate contentKey="luulposApp.ProfileType.SYSTEM_MANAGER" />
                    </option>
                    <option value="COMPANY_MANAGER">
                      <Translate contentKey="luulposApp.ProfileType.COMPANY_MANAGER" />
                    </option>
                    <option value="SHOP_MANAGER">
                      <Translate contentKey="luulposApp.ProfileType.SHOP_MANAGER" />
                    </option>
                    <option value="EMPLOYEE">
                      <Translate contentKey="luulposApp.ProfileType.EMPLOYEE" />
                    </option>
                    <option value="CUSTOMER">
                      <Translate contentKey="luulposApp.ProfileType.CUSTOMER" />
                    </option>
                    <option value="SUPPLIER">
                      <Translate contentKey="luulposApp.ProfileType.SUPPLIER" />
                    </option>
                    <option value="MANAGEMENT_CEO">
                      <Translate contentKey="luulposApp.ProfileType.MANAGEMENT_CEO" />
                    </option>
                    <option value="MANAGEMENT_OTHER">
                      <Translate contentKey="luulposApp.ProfileType.MANAGEMENT_OTHER" />
                    </option>
                    <option value="ACCOUNTANT">
                      <Translate contentKey="luulposApp.ProfileType.ACCOUNTANT" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="dateOfBirthLabel" for="dateOfBirth">
                    <Translate contentKey="luulposApp.profile.dateOfBirth">Date Of Birth</Translate>
                  </Label>
                  <AvField id="profile-dateOfBirth" type="date" className="form-control" name="dateOfBirth" />
                </AvGroup>
                <AvGroup>
                  <Label id="genderLabel">
                    <Translate contentKey="luulposApp.profile.gender">Gender</Translate>
                  </Label>
                  <AvInput
                    id="profile-gender"
                    type="select"
                    className="form-control"
                    name="gender"
                    value={(!isNew && profileEntity.gender) || 'MALE'}
                  >
                    <option value="MALE">
                      <Translate contentKey="luulposApp.Gender.MALE" />
                    </option>
                    <option value="FEMALE">
                      <Translate contentKey="luulposApp.Gender.FEMALE" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="registerationDateLabel" for="registerationDate">
                    <Translate contentKey="luulposApp.profile.registerationDate">Registeration Date</Translate>
                  </Label>
                  <AvInput
                    id="profile-registerationDate"
                    type="datetime-local"
                    className="form-control"
                    name="registerationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.profileEntity.registerationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lastAccessLabel" for="lastAccess">
                    <Translate contentKey="luulposApp.profile.lastAccess">Last Access</Translate>
                  </Label>
                  <AvInput
                    id="profile-lastAccess"
                    type="datetime-local"
                    className="form-control"
                    name="lastAccess"
                    value={isNew ? null : convertDateTimeFromServer(this.props.profileEntity.lastAccess)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="profileStatusLabel">
                    <Translate contentKey="luulposApp.profile.profileStatus">Profile Status</Translate>
                  </Label>
                  <AvInput
                    id="profile-profileStatus"
                    type="select"
                    className="form-control"
                    name="profileStatus"
                    value={(!isNew && profileEntity.profileStatus) || 'ACTIVE'}
                  >
                    <option value="ACTIVE">
                      <Translate contentKey="luulposApp.ProfileStatus.ACTIVE" />
                    </option>
                    <option value="SUSPENDED">
                      <Translate contentKey="luulposApp.ProfileStatus.SUSPENDED" />
                    </option>
                    <option value="IN_ACTIVE">
                      <Translate contentKey="luulposApp.ProfileStatus.IN_ACTIVE" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="telephoneLabel" for="telephone">
                    <Translate contentKey="luulposApp.profile.telephone">Telephone</Translate>
                  </Label>
                  <AvField id="profile-telephone" type="text" name="telephone" />
                </AvGroup>
                <AvGroup>
                  <Label id="mobileLabel" for="mobile">
                    <Translate contentKey="luulposApp.profile.mobile">Mobile</Translate>
                  </Label>
                  <AvField id="profile-mobile" type="text" name="mobile" />
                </AvGroup>
                <AvGroup>
                  <Label id="hourlyPayRateLabel" for="hourlyPayRate">
                    <Translate contentKey="luulposApp.profile.hourlyPayRate">Hourly Pay Rate</Translate>
                  </Label>
                  <AvField id="profile-hourlyPayRate" type="text" name="hourlyPayRate" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="thumbnailPhotoLabel" for="thumbnailPhoto">
                      <Translate contentKey="luulposApp.profile.thumbnailPhoto">Thumbnail Photo</Translate>
                    </Label>
                    <br />
                    {thumbnailPhoto ? (
                      <div>
                        <a onClick={openFile(thumbnailPhotoContentType, thumbnailPhoto)}>
                          <img src={`data:${thumbnailPhotoContentType};base64,${thumbnailPhoto}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {thumbnailPhotoContentType}, {byteSize(thumbnailPhoto)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('thumbnailPhoto')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_thumbnailPhoto" type="file" onChange={this.onBlobChange(true, 'thumbnailPhoto')} accept="image/*" />
                    <AvInput type="hidden" name="thumbnailPhoto" value={thumbnailPhoto} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="thumbnailPhotoUrlLabel" for="thumbnailPhotoUrl">
                    <Translate contentKey="luulposApp.profile.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
                  </Label>
                  <AvField id="profile-thumbnailPhotoUrl" type="text" name="thumbnailPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="fullPhotoLabel" for="fullPhoto">
                      <Translate contentKey="luulposApp.profile.fullPhoto">Full Photo</Translate>
                    </Label>
                    <br />
                    {fullPhoto ? (
                      <div>
                        <a onClick={openFile(fullPhotoContentType, fullPhoto)}>
                          <img src={`data:${fullPhotoContentType};base64,${fullPhoto}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {fullPhotoContentType}, {byteSize(fullPhoto)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('fullPhoto')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_fullPhoto" type="file" onChange={this.onBlobChange(true, 'fullPhoto')} accept="image/*" />
                    <AvInput type="hidden" name="fullPhoto" value={fullPhoto} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="fullPhotoUrlLabel" for="fullPhotoUrl">
                    <Translate contentKey="luulposApp.profile.fullPhotoUrl">Full Photo Url</Translate>
                  </Label>
                  <AvField id="profile-fullPhotoUrl" type="text" name="fullPhotoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="activeLabel" check>
                    <AvInput id="profile-active" type="checkbox" className="form-control" name="active" />
                    <Translate contentKey="luulposApp.profile.active">Active</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="shopChangeIdLabel" for="shopChangeId">
                    <Translate contentKey="luulposApp.profile.shopChangeId">Shop Change Id</Translate>
                  </Label>
                  <AvField id="profile-shopChangeId" type="string" className="form-control" name="shopChangeId" />
                </AvGroup>
                <AvGroup>
                  <Label for="user.firstName">
                    <Translate contentKey="luulposApp.profile.user">User</Translate>
                  </Label>
                  <AvInput id="profile-user" type="select" className="form-control" name="userId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.firstName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="shop.shopName">
                    <Translate contentKey="luulposApp.profile.shop">Shop</Translate>
                  </Label>
                  <AvInput id="profile-shop" type="select" className="form-control" name="shopId">
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
                <Button tag={Link} id="cancel-save" to="/entity/profile" replace color="info">
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
  users: storeState.userManagement.users,
  shops: storeState.shop.entities,
  profileEntity: storeState.profile.entity,
  loading: storeState.profile.loading,
  updating: storeState.profile.updating,
  updateSuccess: storeState.profile.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getShops,
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
)(ProfileUpdate);
