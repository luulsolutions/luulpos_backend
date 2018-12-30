import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProfileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProfileDetail extends React.Component<IProfileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { profileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.profile.detail.title">Profile</Translate> [<b>{profileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="firstName">
                <Translate contentKey="luulposApp.profile.firstName">First Name</Translate>
              </span>
            </dt>
            <dd>{profileEntity.firstName}</dd>
            <dt>
              <span id="lastName">
                <Translate contentKey="luulposApp.profile.lastName">Last Name</Translate>
              </span>
            </dt>
            <dd>{profileEntity.lastName}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="luulposApp.profile.email">Email</Translate>
              </span>
            </dt>
            <dd>{profileEntity.email}</dd>
            <dt>
              <span id="userType">
                <Translate contentKey="luulposApp.profile.userType">User Type</Translate>
              </span>
            </dt>
            <dd>{profileEntity.userType}</dd>
            <dt>
              <span id="dateOfBirth">
                <Translate contentKey="luulposApp.profile.dateOfBirth">Date Of Birth</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={profileEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="gender">
                <Translate contentKey="luulposApp.profile.gender">Gender</Translate>
              </span>
            </dt>
            <dd>{profileEntity.gender}</dd>
            <dt>
              <span id="registerationDate">
                <Translate contentKey="luulposApp.profile.registerationDate">Registeration Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={profileEntity.registerationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastAccess">
                <Translate contentKey="luulposApp.profile.lastAccess">Last Access</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={profileEntity.lastAccess} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="profileStatus">
                <Translate contentKey="luulposApp.profile.profileStatus">Profile Status</Translate>
              </span>
            </dt>
            <dd>{profileEntity.profileStatus}</dd>
            <dt>
              <span id="telephone">
                <Translate contentKey="luulposApp.profile.telephone">Telephone</Translate>
              </span>
            </dt>
            <dd>{profileEntity.telephone}</dd>
            <dt>
              <span id="mobile">
                <Translate contentKey="luulposApp.profile.mobile">Mobile</Translate>
              </span>
            </dt>
            <dd>{profileEntity.mobile}</dd>
            <dt>
              <span id="hourlyPayRate">
                <Translate contentKey="luulposApp.profile.hourlyPayRate">Hourly Pay Rate</Translate>
              </span>
            </dt>
            <dd>{profileEntity.hourlyPayRate}</dd>
            <dt>
              <span id="thumbnailPhoto">
                <Translate contentKey="luulposApp.profile.thumbnailPhoto">Thumbnail Photo</Translate>
              </span>
            </dt>
            <dd>
              {profileEntity.thumbnailPhoto ? (
                <div>
                  <a onClick={openFile(profileEntity.thumbnailPhotoContentType, profileEntity.thumbnailPhoto)}>
                    <img
                      src={`data:${profileEntity.thumbnailPhotoContentType};base64,${profileEntity.thumbnailPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {profileEntity.thumbnailPhotoContentType}, {byteSize(profileEntity.thumbnailPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="thumbnailPhotoUrl">
                <Translate contentKey="luulposApp.profile.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
              </span>
            </dt>
            <dd>{profileEntity.thumbnailPhotoUrl}</dd>
            <dt>
              <span id="fullPhoto">
                <Translate contentKey="luulposApp.profile.fullPhoto">Full Photo</Translate>
              </span>
            </dt>
            <dd>
              {profileEntity.fullPhoto ? (
                <div>
                  <a onClick={openFile(profileEntity.fullPhotoContentType, profileEntity.fullPhoto)}>
                    <img
                      src={`data:${profileEntity.fullPhotoContentType};base64,${profileEntity.fullPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {profileEntity.fullPhotoContentType}, {byteSize(profileEntity.fullPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="fullPhotoUrl">
                <Translate contentKey="luulposApp.profile.fullPhotoUrl">Full Photo Url</Translate>
              </span>
            </dt>
            <dd>{profileEntity.fullPhotoUrl}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="luulposApp.profile.active">Active</Translate>
              </span>
            </dt>
            <dd>{profileEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <span id="shopChangeId">
                <Translate contentKey="luulposApp.profile.shopChangeId">Shop Change Id</Translate>
              </span>
            </dt>
            <dd>{profileEntity.shopChangeId}</dd>
            <dt>
              <Translate contentKey="luulposApp.profile.user">User</Translate>
            </dt>
            <dd>{profileEntity.userFirstName ? profileEntity.userFirstName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.profile.shop">Shop</Translate>
            </dt>
            <dd>{profileEntity.shopShopName ? profileEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/profile" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/profile/${profileEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ profile }: IRootState) => ({
  profileEntity: profile.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProfileDetail);
