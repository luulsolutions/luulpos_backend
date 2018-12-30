import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment.reducer';
import { IPayment } from 'app/shared/model/payment.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PaymentDetail extends React.Component<IPaymentDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { paymentEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.payment.detail.title">Payment</Translate> [<b>{paymentEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="paymentDate">
                <Translate contentKey="luulposApp.payment.paymentDate">Payment Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={paymentEntity.paymentDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="paymentProvider">
                <Translate contentKey="luulposApp.payment.paymentProvider">Payment Provider</Translate>
              </span>
            </dt>
            <dd>{paymentEntity.paymentProvider}</dd>
            <dt>
              <span id="amount">
                <Translate contentKey="luulposApp.payment.amount">Amount</Translate>
              </span>
            </dt>
            <dd>{paymentEntity.amount}</dd>
            <dt>
              <span id="paymentStatus">
                <Translate contentKey="luulposApp.payment.paymentStatus">Payment Status</Translate>
              </span>
            </dt>
            <dd>{paymentEntity.paymentStatus}</dd>
            <dt>
              <span id="curency">
                <Translate contentKey="luulposApp.payment.curency">Curency</Translate>
              </span>
            </dt>
            <dd>{paymentEntity.curency}</dd>
            <dt>
              <span id="customerName">
                <Translate contentKey="luulposApp.payment.customerName">Customer Name</Translate>
              </span>
            </dt>
            <dd>{paymentEntity.customerName}</dd>
            <dt>
              <Translate contentKey="luulposApp.payment.shop">Shop</Translate>
            </dt>
            <dd>{paymentEntity.shopShopName ? paymentEntity.shopShopName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.payment.processedBy">Processed By</Translate>
            </dt>
            <dd>{paymentEntity.processedByFirstName ? paymentEntity.processedByFirstName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.payment.paymentMethod">Payment Method</Translate>
            </dt>
            <dd>{paymentEntity.paymentMethodPaymentMethod ? paymentEntity.paymentMethodPaymentMethod : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.payment.order">Order</Translate>
            </dt>
            <dd>{paymentEntity.orderDescription ? paymentEntity.orderDescription : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/payment" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/payment/${paymentEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ payment }: IRootState) => ({
  paymentEntity: payment.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PaymentDetail);
