import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment-method-config.reducer';
import { IPaymentMethodConfig } from 'app/shared/model/payment-method-config.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentMethodConfigDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PaymentMethodConfigDetail extends React.Component<IPaymentMethodConfigDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { paymentMethodConfigEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.paymentMethodConfig.detail.title">PaymentMethodConfig</Translate> [<b>
              {paymentMethodConfigEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="key">
                <Translate contentKey="luulposApp.paymentMethodConfig.key">Key</Translate>
              </span>
            </dt>
            <dd>{paymentMethodConfigEntity.key}</dd>
            <dt>
              <span id="value">
                <Translate contentKey="luulposApp.paymentMethodConfig.value">Value</Translate>
              </span>
            </dt>
            <dd>{paymentMethodConfigEntity.value}</dd>
            <dt>
              <span id="note">
                <Translate contentKey="luulposApp.paymentMethodConfig.note">Note</Translate>
              </span>
            </dt>
            <dd>{paymentMethodConfigEntity.note}</dd>
            <dt>
              <span id="enabled">
                <Translate contentKey="luulposApp.paymentMethodConfig.enabled">Enabled</Translate>
              </span>
            </dt>
            <dd>{paymentMethodConfigEntity.enabled ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="luulposApp.paymentMethodConfig.paymentMethod">Payment Method</Translate>
            </dt>
            <dd>{paymentMethodConfigEntity.paymentMethodPaymentMethod ? paymentMethodConfigEntity.paymentMethodPaymentMethod : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/payment-method-config" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/payment-method-config/${paymentMethodConfigEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ paymentMethodConfig }: IRootState) => ({
  paymentMethodConfigEntity: paymentMethodConfig.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PaymentMethodConfigDetail);
