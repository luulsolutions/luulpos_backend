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
import { getSearchEntities, getEntities } from './product.reducer';
import { IProduct } from 'app/shared/model/product.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IProductProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IProductState extends IPaginationBaseState {
  search: string;
}

export class Product extends React.Component<IProductProps, IProductState> {
  state: IProductState = {
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
    const { productList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="product-heading">
          <Translate contentKey="luulposApp.product.home.title">Products</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.product.home.createLabel">Create new Product</Translate>
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
                    placeholder={translate('luulposApp.product.home.search')}
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
                <th className="hand" onClick={this.sort('productName')}>
                  <Translate contentKey="luulposApp.product.productName">Product Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('productDescription')}>
                  <Translate contentKey="luulposApp.product.productDescription">Product Description</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('price')}>
                  <Translate contentKey="luulposApp.product.price">Price</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('quantity')}>
                  <Translate contentKey="luulposApp.product.quantity">Quantity</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('productImageFull')}>
                  <Translate contentKey="luulposApp.product.productImageFull">Product Image Full</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('productImageFullUrl')}>
                  <Translate contentKey="luulposApp.product.productImageFullUrl">Product Image Full Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('productImageThumb')}>
                  <Translate contentKey="luulposApp.product.productImageThumb">Product Image Thumb</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('productImageThumbUrl')}>
                  <Translate contentKey="luulposApp.product.productImageThumbUrl">Product Image Thumb Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('dateCreated')}>
                  <Translate contentKey="luulposApp.product.dateCreated">Date Created</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('barcode')}>
                  <Translate contentKey="luulposApp.product.barcode">Barcode</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('serialCode')}>
                  <Translate contentKey="luulposApp.product.serialCode">Serial Code</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('priorityPosition')}>
                  <Translate contentKey="luulposApp.product.priorityPosition">Priority Position</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('active')}>
                  <Translate contentKey="luulposApp.product.active">Active</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('isVariantProduct')}>
                  <Translate contentKey="luulposApp.product.isVariantProduct">Is Variant Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.product.productTypes">Product Types</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.product.shop">Shop</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.product.discounts">Discounts</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.product.taxes">Taxes</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.product.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productList.map((product, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${product.id}`} color="link" size="sm">
                      {product.id}
                    </Button>
                  </td>
                  <td>{product.productName}</td>
                  <td>{product.productDescription}</td>
                  <td>{product.price}</td>
                  <td>{product.quantity}</td>
                  <td>
                    {product.productImageFull ? (
                      <div>
                        <a onClick={openFile(product.productImageFullContentType, product.productImageFull)}>
                          <img
                            src={`data:${product.productImageFullContentType};base64,${product.productImageFull}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {product.productImageFullContentType}, {byteSize(product.productImageFull)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{product.productImageFullUrl}</td>
                  <td>
                    {product.productImageThumb ? (
                      <div>
                        <a onClick={openFile(product.productImageThumbContentType, product.productImageThumb)}>
                          <img
                            src={`data:${product.productImageThumbContentType};base64,${product.productImageThumb}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {product.productImageThumbContentType}, {byteSize(product.productImageThumb)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{product.productImageThumbUrl}</td>
                  <td>
                    <TextFormat type="date" value={product.dateCreated} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{product.barcode}</td>
                  <td>{product.serialCode}</td>
                  <td>{product.priorityPosition}</td>
                  <td>{product.active ? 'true' : 'false'}</td>
                  <td>{product.isVariantProduct ? 'true' : 'false'}</td>
                  <td>
                    {product.productTypesProductType ? (
                      <Link to={`product-type/${product.productTypesId}`}>{product.productTypesProductType}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{product.shopShopName ? <Link to={`shop/${product.shopId}`}>{product.shopShopName}</Link> : ''}</td>
                  <td>
                    {product.discountsDescription ? <Link to={`discount/${product.discountsId}`}>{product.discountsDescription}</Link> : ''}
                  </td>
                  <td>{product.taxesDescription ? <Link to={`tax/${product.taxesId}`}>{product.taxesDescription}</Link> : ''}</td>
                  <td>
                    {product.categoryCategory ? <Link to={`product-category/${product.categoryId}`}>{product.categoryCategory}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${product.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${product.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${product.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ product }: IRootState) => ({
  productList: product.entities,
  totalItems: product.totalItems
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
)(Product);
