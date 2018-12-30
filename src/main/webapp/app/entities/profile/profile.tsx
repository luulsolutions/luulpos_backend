import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import {
  openFile,
  byteSize,
  Translate,
  translate,
  ICrudSearchAction,
  ICrudGetAllAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IProfileProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IProfileState extends IPaginationBaseState {
  search: string;
}

export class Profile extends React.Component<IProfileProps, IProfileState> {
  state: IProfileState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.props.getEntities();
    this.setState({
      search: ''
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { profileList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="profile-heading">
          <Translate contentKey="luulposApp.profile.home.title">Profiles</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.profile.home.createLabel">Create new Profile</Translate>
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput
                    type="text"
                    name="search"
                    value={this.state.search}
                    onChange={this.handleSearch}
                    placeholder={translate('luulposApp.profile.home.search')}
                  />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('firstName')}>
                  <Translate contentKey="luulposApp.profile.firstName">First Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('lastName')}>
                  <Translate contentKey="luulposApp.profile.lastName">Last Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('email')}>
                  <Translate contentKey="luulposApp.profile.email">Email</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('userType')}>
                  <Translate contentKey="luulposApp.profile.userType">User Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('dateOfBirth')}>
                  <Translate contentKey="luulposApp.profile.dateOfBirth">Date Of Birth</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('gender')}>
                  <Translate contentKey="luulposApp.profile.gender">Gender</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('registerationDate')}>
                  <Translate contentKey="luulposApp.profile.registerationDate">Registeration Date</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('lastAccess')}>
                  <Translate contentKey="luulposApp.profile.lastAccess">Last Access</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('profileStatus')}>
                  <Translate contentKey="luulposApp.profile.profileStatus">Profile Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('telephone')}>
                  <Translate contentKey="luulposApp.profile.telephone">Telephone</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('mobile')}>
                  <Translate contentKey="luulposApp.profile.mobile">Mobile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('hourlyPayRate')}>
                  <Translate contentKey="luulposApp.profile.hourlyPayRate">Hourly Pay Rate</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhoto')}>
                  <Translate contentKey="luulposApp.profile.thumbnailPhoto">Thumbnail Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhotoUrl')}>
                  <Translate contentKey="luulposApp.profile.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhoto')}>
                  <Translate contentKey="luulposApp.profile.fullPhoto">Full Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhotoUrl')}>
                  <Translate contentKey="luulposApp.profile.fullPhotoUrl">Full Photo Url</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('active')}>
                  <Translate contentKey="luulposApp.profile.active">Active</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('shopChangeId')}>
                  <Translate contentKey="luulposApp.profile.shopChangeId">Shop Change Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.profile.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.profile.shop">Shop</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {profileList.map((profile, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${profile.id}`} color="link" size="sm">
                      {profile.id}
                    </Button>
                  </td>
                  <td>{profile.firstName}</td>
                  <td>{profile.lastName}</td>
                  <td>{profile.email}</td>
                  <td>
                    <Translate contentKey={`luulposApp.ProfileType.${profile.userType}`} />
                  </td>
                  <td>
                    <TextFormat type="date" value={profile.dateOfBirth} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <Translate contentKey={`luulposApp.Gender.${profile.gender}`} />
                  </td>
                  <td>
                    <TextFormat type="date" value={profile.registerationDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={profile.lastAccess} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <Translate contentKey={`luulposApp.ProfileStatus.${profile.profileStatus}`} />
                  </td>
                  <td>{profile.telephone}</td>
                  <td>{profile.mobile}</td>
                  <td>{profile.hourlyPayRate}</td>
                  <td>
                    {profile.thumbnailPhoto ? (
                      <div>
                        <a onClick={openFile(profile.thumbnailPhotoContentType, profile.thumbnailPhoto)}>
                          <img
                            src={`data:${profile.thumbnailPhotoContentType};base64,${profile.thumbnailPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {profile.thumbnailPhotoContentType}, {byteSize(profile.thumbnailPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{profile.thumbnailPhotoUrl}</td>
                  <td>
                    {profile.fullPhoto ? (
                      <div>
                        <a onClick={openFile(profile.fullPhotoContentType, profile.fullPhoto)}>
                          <img src={`data:${profile.fullPhotoContentType};base64,${profile.fullPhoto}`} style={{ maxHeight: '30px' }} />
                          &nbsp;
                        </a>
                        <span>
                          {profile.fullPhotoContentType}, {byteSize(profile.fullPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{profile.fullPhotoUrl}</td>
                  <td>{profile.active ? 'true' : 'false'}</td>
                  <td>{profile.shopChangeId}</td>
                  <td>{profile.userFirstName ? profile.userFirstName : ''}</td>
                  <td>{profile.shopShopName ? <Link to={`shop/${profile.shopId}`}>{profile.shopShopName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${profile.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${profile.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${profile.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ profile }: IRootState) => ({
  profileList: profile.entities,
  totalItems: profile.totalItems
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Profile);
