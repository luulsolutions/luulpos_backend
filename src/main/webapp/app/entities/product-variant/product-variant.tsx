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
import { getSearchEntities, getEntities } from './product-variant.reducer';
import { IProductVariant } from 'app/shared/model/product-variant.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IProductVariantProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IProductVariantState extends IPaginationBaseState {
  search: string;
}

export class ProductVariant extends React.Component<IProductVariantProps, IProductVariantState> {
  state: IProductVariantState = {
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
    const { productVariantList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="product-variant-heading">
          <Translate contentKey="luulposApp.productVariant.home.title">Product Variants</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="luulposApp.productVariant.home.createLabel">Create new Product Variant</Translate>
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
                    placeholder={translate('luulposApp.productVariant.home.search')}
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
                  <Translate contentKey="luulposApp.productVariant.variantName">Variant Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('description')}>
                  <Translate contentKey="luulposApp.productVariant.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('percentage')}>
                  <Translate contentKey="luulposApp.productVariant.percentage">Percentage</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhoto')}>
                  <Translate contentKey="luulposApp.productVariant.fullPhoto">Full Photo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fullPhotoUrl')}>
                  <Translate contentKey="luulposApp.productVariant.fullPhotoUrl">Full Photo Url</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhoto')}>
                  <Translate contentKey="luulposApp.productVariant.thumbnailPhoto">Thumbnail Photo</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('thumbnailPhotoUrl')}>
                  <Translate contentKey="luulposApp.productVariant.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('price')}>
                  <Translate contentKey="luulposApp.productVariant.price">Price</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="luulposApp.productVariant.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productVariantList.map((productVariant, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${productVariant.id}`} color="link" size="sm">
                      {productVariant.id}
                    </Button>
                  </td>
                  <td>{productVariant.variantName}</td>
                  <td>{productVariant.description}</td>
                  <td>{productVariant.percentage}</td>
                  <td>
                    {productVariant.fullPhoto ? (
                      <div>
                        <a onClick={openFile(productVariant.fullPhotoContentType, productVariant.fullPhoto)}>
                          <img
                            src={`data:${productVariant.fullPhotoContentType};base64,${productVariant.fullPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {productVariant.fullPhotoContentType}, {byteSize(productVariant.fullPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{productVariant.fullPhotoUrl}</td>
                  <td>
                    {productVariant.thumbnailPhoto ? (
                      <div>
                        <a onClick={openFile(productVariant.thumbnailPhotoContentType, productVariant.thumbnailPhoto)}>
                          <img
                            src={`data:${productVariant.thumbnailPhotoContentType};base64,${productVariant.thumbnailPhoto}`}
                            style={{ maxHeight: '30px' }}
                          />
                          &nbsp;
                        </a>
                        <span>
                          {productVariant.thumbnailPhotoContentType}, {byteSize(productVariant.thumbnailPhoto)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{productVariant.thumbnailPhotoUrl}</td>
                  <td>{productVariant.price}</td>
                  <td>
                    {productVariant.productProductName ? (
                      <Link to={`product/${productVariant.productId}`}>{productVariant.productProductName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${productVariant.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productVariant.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${productVariant.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ productVariant }: IRootState) => ({
  productVariantList: productVariant.entities,
  totalItems: productVariant.totalItems
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
)(ProductVariant);
