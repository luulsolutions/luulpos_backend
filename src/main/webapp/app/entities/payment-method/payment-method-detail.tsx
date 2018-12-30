import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment-method.reducer';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentMethodDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PaymentMethodDetail extends React.Component<IPaymentMethodDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { paymentMethodEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.paymentMethod.detail.title">PaymentMethod</Translate> [<b>{paymentMethodEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="paymentMethod">
                <Translate contentKey="luulposApp.paymentMethod.paymentMethod">Payment Method</Translate>
              </span>
            </dt>
            <dd>{paymentMethodEntity.paymentMethod}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.paymentMethod.description">Description</Translate>
              </span>
            </dt>
            <dd>{paymentMethodEntity.description}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="luulposApp.paymentMethod.active">Active</Translate>
              </span>
            </dt>
            <dd>{paymentMethodEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="luulposApp.paymentMethod.shop">Shop</Translate>
            </dt>
            <dd>{paymentMethodEntity.shopShopName ? paymentMethodEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/payment-method" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/payment-method/${paymentMethodEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ paymentMethod }: IRootState) => ({
  paymentMethodEntity: paymentMethod.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PaymentMethodDetail);
