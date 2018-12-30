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
import { getSearchEntities, getEntities } from './shop.reducer';
import { IShop } from 'app/shared/model/shop.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IShopProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IShopState extends IPaginationBaseState {
  search: string;
}

export class Shop extends React.Component<IShopProps, IShopState> {
  state: IShopState = {
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
    const { shopList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="shop-heading">
          <Translate contentKey="luulposApp.shop.home.title">Shops</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.shop.home.createLabel">Create new Shop</Translate>
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
                    placeholder={translate('luulposApp.shop.home.search')}
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
                <th className="hand" onClick={this.sort('shopName')}>
                  <Translate contentKey="luulposApp.shop.shopName">Shop Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('shopAccountType')}>
                  <Translate contentKey="luulposApp.shop.shopAccountType">Shop Account Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('approvalDate')}>
                  <Translate contentKey="luulposApp.shop.approvalDate">Approval Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('address')}>
                  <Translate contentKey="luulposApp.shop.address">Address</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('email')}>
                  <Translate contentKey="luulposApp.shop.email">Email</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('description')}>
                  <Translate contentKey="luulposApp.shop.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('note')}>
                  <Translate contentKey="luulposApp.shop.note">Note</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('landland')}>
                  <Translate contentKey="luulposApp.shop.landland">Landland</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('mobile')}>
                  <Translate contentKey="luulposApp.shop.mobile">Mobile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('createdDate')}>
                  <Translate contentKey="luulposApp.shop.createdDate">Created Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('shopLogo')}>
                  <Translate contentKey="luulposApp.shop.shopLogo">Shop Logo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('shopLogoUrl')}>
                  <Translate contentKey="luulposApp.shop.shopLogoUrl">Shop Logo Url</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('active')}>
                  <Translate contentKey="luulposApp.shop.active">Active</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('currency')}>
                  <Translate contentKey="luulposApp.shop.currency">Currency</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.shop.company">Company</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.shop.approvedBy">Approved By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shopList.map((shop, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${shop.id}`} color="link" size="sm">
                      {shop.id}
                    </Button>
                  </td>
                  <td>{shop.shopName}</td>
                  <td>
                    <Translate contentKey={`luulposApp.ShopAccountType.${shop.shopAccountType}`} />
                  </td>
                  <td>
                    <TextFormat type="date" value={shop.approvalDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{shop.address}</td>
                  <td>{shop.email}</td>
                  <td>{shop.description}</td>
                  <td>{shop.note}</td>
                  <td>{shop.landland}</td>
                  <td>{shop.mobile}</td>
                  <td>
                    <TextFormat type="date" value={shop.createdDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    {shop.shopLogo ? (
                      <div>
                        <a onClick={openFile(shop.shopLogoContentType, shop.shopLogo)}>
                          <img src={`data:${shop.shopLogoContentType};base64,${shop.shopLogo}`} style={{ maxHeight: '30px' }} />
                          &nbsp;
                        </a>
                        <span>
                          {shop.shopLogoContentType}, {byteSize(shop.shopLogo)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{shop.shopLogoUrl}</td>
                  <td>{shop.active ? 'true' : 'false'}</td>
                  <td>{shop.currency}</td>
                  <td>{shop.companyCompanyName ? <Link to={`company/${shop.companyId}`}>{shop.companyCompanyName}</Link> : ''}</td>
                  <td>{shop.approvedByFirstName ? <Link to={`profile/${shop.approvedById}`}>{shop.approvedByFirstName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${shop.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shop.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shop.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ shop }: IRootState) => ({
  shopList: shop.entities,
  totalItems: shop.totalItems
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
)(Shop);
