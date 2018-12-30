import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-extra.reducer';
import { IProductExtra } from 'app/shared/model/product-extra.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductExtraDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProductExtraDetail extends React.Component<IProductExtraDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { productExtraEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.productExtra.detail.title">ProductExtra</Translate> [<b>{productExtraEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="extraName">
                <Translate contentKey="luulposApp.productExtra.extraName">Extra Name</Translate>
              </span>
            </dt>
            <dd>{productExtraEntity.extraName}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.productExtra.description">Description</Translate>
              </span>
            </dt>
            <dd>{productExtraEntity.description}</dd>
            <dt>
              <span id="extraValue">
                <Translate contentKey="luulposApp.productExtra.extraValue">Extra Value</Translate>
              </span>
            </dt>
            <dd>{productExtraEntity.extraValue}</dd>
            <dt>
              <span id="fullPhoto">
                <Translate contentKey="luulposApp.productExtra.fullPhoto">Full Photo</Translate>
              </span>
            </dt>
            <dd>
              {productExtraEntity.fullPhoto ? (
                <div>
                  <a onClick={openFile(productExtraEntity.fullPhotoContentType, productExtraEntity.fullPhoto)}>
                    <img
                      src={`data:${productExtraEntity.fullPhotoContentType};base64,${productExtraEntity.fullPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {productExtraEntity.fullPhotoContentType}, {byteSize(productExtraEntity.fullPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="fullPhotoUrl">
                <Translate contentKey="luulposApp.productExtra.fullPhotoUrl">Full Photo Url</Translate>
              </span>
            </dt>
            <dd>{productExtraEntity.fullPhotoUrl}</dd>
            <dt>
              <span id="thumbnailPhoto">
                <Translate contentKey="luulposApp.productExtra.thumbnailPhoto">Thumbnail Photo</Translate>
              </span>
            </dt>
            <dd>
              {productExtraEntity.thumbnailPhoto ? (
                <div>
                  <a onClick={openFile(productExtraEntity.thumbnailPhotoContentType, productExtraEntity.thumbnailPhoto)}>
                    <img
                      src={`data:${productExtraEntity.thumbnailPhotoContentType};base64,${productExtraEntity.thumbnailPhoto}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {productExtraEntity.thumbnailPhotoContentType}, {byteSize(productExtraEntity.thumbnailPhoto)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="thumbnailPhotoUrl">
                <Translate contentKey="luulposApp.productExtra.thumbnailPhotoUrl">Thumbnail Photo Url</Translate>
              </span>
            </dt>
            <dd>{productExtraEntity.thumbnailPhotoUrl}</dd>
            <dt>
              <Translate contentKey="luulposApp.productExtra.product">Product</Translate>
            </dt>
            <dd>{productExtraEntity.productProductName ? productExtraEntity.productProductName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/product-extra" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/product-extra/${productExtraEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ productExtra }: IRootState) => ({
  productExtraEntity: productExtra.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductExtraDetail);
