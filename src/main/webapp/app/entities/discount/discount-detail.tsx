import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './discount.reducer';
import { IDiscount } from 'app/shared/model/discount.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDiscountDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DiscountDetail extends React.Component<IDiscountDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { discountEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.discount.detail.title">Discount</Translate> [<b>{discountEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.discount.description">Description</Translate>
              </span>
            </dt>
            <dd>{discountEntity.description}</dd>
            <dt>
              <span id="percentage">
                <Translate contentKey="luulposApp.discount.percentage">Percentage</Translate>
              </span>
            </dt>
            <dd>{discountEntity.percentage}</dd>
            <dt>
              <span id="amount">
                <Translate contentKey="luulposApp.discount.amount">Amount</Translate>
              </span>
            </dt>
            <dd>{discountEntity.amount}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="luulposApp.discount.active">Active</Translate>
              </span>
            </dt>
            <dd>{discountEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="luulposApp.discount.shop">Shop</Translate>
            </dt>
            <dd>{discountEntity.shopShopName ? discountEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/discount" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/discount/${discountEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ discount }: IRootState) => ({
  discountEntity: discount.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiscountDetail);
