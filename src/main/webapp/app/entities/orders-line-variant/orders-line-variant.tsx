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
  getSortState,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './orders-line-variant.reducer';
import { IOrdersLineVariant } from 'app/shared/model/orders-line-variant.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IOrdersLineVariantProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IOrdersLineVariantState extends IPaginationBaseState {
  search: string;
}

export class OrdersLineVariant extends React.Component<IOrdersLineVariantProps, IOrdersLineVariantState> {
  state: IOrdersLineVariantState = {
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
    const { ordersLineVariantList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="orders-line-variant-heading">
          <Translate contentKey="luulposApp.ordersLineVariant.home.title">Orders Line Variants</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.ordersLineVariant.home.createLabel">Create new Orders Line Variant</Translate>
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
                    placeholder={translate('luulposApp.ordersLineVariant.home.search')}
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
                <th className="hand" onClick={this.sort('variantName')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.variantName">Variant Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('variantValue')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.variantValue">Variant Value</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('description')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('percentage')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.percentage">Percentage</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhoto')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.fullPhoto">Full Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhotoUrl')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.fullPhotoUrl">Full Photo Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhoto')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.thumbnailPhoto">Thumbnail Photo</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhotoUrl')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('price')}>
                  <Translate contentKey="luulposApp.ordersLineVariant.price">Price</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.ordersLineVariant.ordersLine">Orders Line</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ordersLineVariantList.map((ordersLineVariant, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${ordersLineVariant.id}`} color="link" size="sm">
                      {ordersLineVariant.id}
                    </Button>
                  </td>
                  <td>{ordersLineVariant.variantName}</td>
                  <td>{ordersLineVariant.variantValue}</td>
                  <td>{ordersLineVariant.description}</td>
                  <td>{ordersLineVariant.percentage}</td>
                  <td>
                    {ordersLineVariant.fullPhoto ? (
                      <div>
                        <a onClick={openFile(ordersLineVariant.fullPhotoContentType, ordersLineVariant.fullPhoto)}>
                          <img
                            src={`data:${ordersLineVariant.fullPhotoContentType};base64,${ordersLineVariant.fullPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {ordersLineVariant.fullPhotoContentType}, {byteSize(ordersLineVariant.fullPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{ordersLineVariant.fullPhotoUrl}</td>
                  <td>
                    {ordersLineVariant.thumbnailPhoto ? (
                      <div>
                        <a onClick={openFile(ordersLineVariant.thumbnailPhotoContentType, ordersLineVariant.thumbnailPhoto)}>
                          <img
                            src={`data:${ordersLineVariant.thumbnailPhotoContentType};base64,${ordersLineVariant.thumbnailPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {ordersLineVariant.thumbnailPhotoContentType}, {byteSize(ordersLineVariant.thumbnailPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{ordersLineVariant.thumbnailPhotoUrl}</td>
                  <td>{ordersLineVariant.price}</td>
                  <td>
                    {ordersLineVariant.ordersLineId ? (
                      <Link to={`orders-line/${ordersLineVariant.ordersLineId}`}>{ordersLineVariant.ordersLineId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${ordersLineVariant.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ordersLineVariant.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ordersLineVariant.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ ordersLineVariant }: IRootState) => ({
  ordersLineVariantList: ordersLineVariant.entities,
  totalItems: ordersLineVariant.totalItems
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
)(OrdersLineVariant);
