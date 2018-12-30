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
import { getSearchEntities, getEntities } from './orders-line-extra.reducer';
import { IOrdersLineExtra } from 'app/shared/model/orders-line-extra.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IOrdersLineExtraProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IOrdersLineExtraState extends IPaginationBaseState {
  search: string;
}

export class OrdersLineExtra extends React.Component<IOrdersLineExtraProps, IOrdersLineExtraState> {
  state: IOrdersLineExtraState = {
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
    const { ordersLineExtraList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="orders-line-extra-heading">
          <Translate contentKey="luulposApp.ordersLineExtra.home.title">Orders Line Extras</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.ordersLineExtra.home.createLabel">Create new Orders Line Extra</Translate>
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
                    placeholder={translate('luulposApp.ordersLineExtra.home.search')}
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
                <th className="hand" onClick={this.sort('ordersLineExtraName')}>
                  <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraName">Orders Line Extra Name</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('ordersLineExtraValue')}>
                  <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraValue">Orders Line Extra Value</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('ordersLineExtraPrice')}>
                  <Translate contentKey="luulposApp.ordersLineExtra.ordersLineExtraPrice">Orders Line Extra Price</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('ordersOptionDescription')}>
                  <Translate contentKey="luulposApp.ordersLineExtra.ordersOptionDescription">Orders Option Description</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhoto')}>
                  <Translate contentKey="luulposApp.ordersLineExtra.fullPhoto">Full Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhotoUrl')}>
                  <Translate contentKey="luulposApp.ordersLineExtra.fullPhotoUrl">Full Photo Url</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhoto')}>
                  <Translate contentKey="luulposApp.ordersLineExtra.thumbnailPhoto">Thumbnail Photo</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhotoUrl')}>
                  <Translate contentKey="luulposApp.ordersLineExtra.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.ordersLineExtra.ordersLineVariant">Orders Line Variant</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ordersLineExtraList.map((ordersLineExtra, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${ordersLineExtra.id}`} color="link" size="sm">
                      {ordersLineExtra.id}
                    </Button>
                  </td>
                  <td>{ordersLineExtra.ordersLineExtraName}</td>
                  <td>{ordersLineExtra.ordersLineExtraValue}</td>
                  <td>{ordersLineExtra.ordersLineExtraPrice}</td>
                  <td>{ordersLineExtra.ordersOptionDescription}</td>
                  <td>
                    {ordersLineExtra.fullPhoto ? (
                      <div>
                        <a onClick={openFile(ordersLineExtra.fullPhotoContentType, ordersLineExtra.fullPhoto)}>
                          <img
                            src={`data:${ordersLineExtra.fullPhotoContentType};base64,${ordersLineExtra.fullPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {ordersLineExtra.fullPhotoContentType}, {byteSize(ordersLineExtra.fullPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{ordersLineExtra.fullPhotoUrl}</td>
                  <td>
                    {ordersLineExtra.thumbnailPhoto ? (
                      <div>
                        <a onClick={openFile(ordersLineExtra.thumbnailPhotoContentType, ordersLineExtra.thumbnailPhoto)}>
                          <img
                            src={`data:${ordersLineExtra.thumbnailPhotoContentType};base64,${ordersLineExtra.thumbnailPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {ordersLineExtra.thumbnailPhotoContentType}, {byteSize(ordersLineExtra.thumbnailPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{ordersLineExtra.thumbnailPhotoUrl}</td>
                  <td>
                    {ordersLineExtra.ordersLineVariantId ? (
                      <Link to={`orders-line-variant/${ordersLineExtra.ordersLineVariantId}`}>{ordersLineExtra.ordersLineVariantId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${ordersLineExtra.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ordersLineExtra.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${ordersLineExtra.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ ordersLineExtra }: IRootState) => ({
  ordersLineExtraList: ordersLineExtra.entities,
  totalItems: ordersLineExtra.totalItems
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
)(OrdersLineExtra);
