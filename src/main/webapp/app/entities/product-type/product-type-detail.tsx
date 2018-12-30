import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-type.reducer';
import { IProductType } from 'app/shared/model/product-type.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductTypeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProductTypeDetail extends React.Component<IProductTypeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { productTypeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.productType.detail.title">ProductType</Translate> [<b>{productTypeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="productType">
                <Translate contentKey="luulposApp.productType.productType">Product Type</Translate>
              </span>
            </dt>
            <dd>{productTypeEntity.productType}</dd>
            <dt>
              <span id="productTypeDescription">
                <Translate contentKey="luulposApp.productType.productTypeDescription">Product Type Description</Translate>
              </span>
            </dt>
            <dd>{productTypeEntity.productTypeDescription}</dd>
            <dt>
              <Translate contentKey="luulposApp.productType.shop">Shop</Translate>
            </dt>
            <dd>{productTypeEntity.shopShopName ? productTypeEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/product-type" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/product-type/${productTypeEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ productType }: IRootState) => ({
  productTypeEntity: productType.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductTypeDetail);
