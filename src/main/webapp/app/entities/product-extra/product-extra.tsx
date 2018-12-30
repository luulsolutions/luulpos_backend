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
import { getSearchEntities, getEntities } from './product-extra.reducer';
import { IProductExtra } from 'app/shared/model/product-extra.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IProductExtraProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IProductExtraState extends IPaginationBaseState {
  search: string;
}

export class ProductExtra extends React.Component<IProductExtraProps, IProductExtraState> {
  state: IProductExtraState = {
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
    const { productExtraList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="product-extra-heading">
          <Translate contentKey="luulposApp.productExtra.home.title">Product Extras</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.productExtra.home.createLabel">Create new Product Extra</Translate>
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
                    placeholder={translate('luulposApp.productExtra.home.search')}
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
                <th className="hand" onClick={this.sort('extraName')}>
                  <Translate contentKey="luulposApp.productExtra.extraName">Extra Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('description')}>
                  <Translate contentKey="luulposApp.productExtra.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('extraValue')}>
                  <Translate contentKey="luulposApp.productExtra.extraValue">Extra Value</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhoto')}>
                  <Translate contentKey="luulposApp.productExtra.fullPhoto">Full Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhotoUrl')}>
                  <Translate contentKey="luulposApp.productExtra.fullPhotoUrl">Full Photo Url</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhoto')}>
                  <Translate contentKey="luulposApp.productExtra.thumbnailPhoto">Thumbnail Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhotoUrl')}>
                  <Translate contentKey="luulposApp.productExtra.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.productExtra.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productExtraList.map((productExtra, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${productExtra.id}`} color="link" size="sm">
                      {productExtra.id}
                    </Button>
                  </td>
                  <td>{productExtra.extraName}</td>
                  <td>{productExtra.description}</td>
                  <td>{productExtra.extraValue}</td>
                  <td>
                    {productExtra.fullPhoto ? (
                      <div>
                        <a onClick={openFile(productExtra.fullPhotoContentType, productExtra.fullPhoto)}>
                          <img
                            src={`data:${productExtra.fullPhotoContentType};base64,${productExtra.fullPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {productExtra.fullPhotoContentType}, {byteSize(productExtra.fullPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{productExtra.fullPhotoUrl}</td>
                  <td>
                    {productExtra.thumbnailPhoto ? (
                      <div>
                        <a onClick={openFile(productExtra.thumbnailPhotoContentType, productExtra.thumbnailPhoto)}>
                          <img
                            src={`data:${productExtra.thumbnailPhotoContentType};base64,${productExtra.thumbnailPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {productExtra.thumbnailPhotoContentType}, {byteSize(productExtra.thumbnailPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{productExtra.thumbnailPhotoUrl}</td>
                  <td>
                    {productExtra.productProductName ? (
                      <Link to={`product/${productExtra.productId}`}>{productExtra.productProductName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${productExtra.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productExtra.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productExtra.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ productExtra }: IRootState) => ({
  productExtraList: productExtra.entities,
  totalItems: productExtra.totalItems
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
)(ProductExtra);
