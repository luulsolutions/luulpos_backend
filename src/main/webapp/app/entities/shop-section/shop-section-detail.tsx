import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop-section.reducer';
import { IShopSection } from 'app/shared/model/shop-section.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopSectionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShopSectionDetail extends React.Component<IShopSectionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shopSectionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.shopSection.detail.title">ShopSection</Translate> [<b>{shopSectionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="sectionName">
                <Translate contentKey="luulposApp.shopSection.sectionName">Section Name</Translate>
              </span>
            </dt>
            <dd>{shopSectionEntity.sectionName}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.shopSection.description">Description</Translate>
              </span>
            </dt>
            <dd>{shopSectionEntity.description}</dd>
            <dt>
              <span id="surchargePercentage">
                <Translate contentKey="luulposApp.shopSection.surchargePercentage">Surcharge Percentage</Translate>
              </span>
            </dt>
            <dd>{shopSectionEntity.surchargePercentage}</dd>
            <dt>
              <span id="surchargeFlatAmount">
                <Translate contentKey="luulposApp.shopSection.surchargeFlatAmount">Surcharge Flat Amount</Translate>
              </span>
            </dt>
            <dd>{shopSectionEntity.surchargeFlatAmount}</dd>
            <dt>
              <span id="usePercentage">
                <Translate contentKey="luulposApp.shopSection.usePercentage">Use Percentage</Translate>
              </span>
            </dt>
            <dd>{shopSectionEntity.usePercentage ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="luulposApp.shopSection.shop">Shop</Translate>
            </dt>
            <dd>{shopSectionEntity.shopShopName ? shopSectionEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/shop-section" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/shop-section/${shopSectionEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ shopSection }: IRootState) => ({
  shopSectionEntity: shopSection.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShopSectionDetail);
