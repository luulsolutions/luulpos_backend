import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './tax.reducer';
import { ITax } from 'app/shared/model/tax.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITaxDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TaxDetail extends React.Component<ITaxDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { taxEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.tax.detail.title">Tax</Translate> [<b>{taxEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.tax.description">Description</Translate>
              </span>
            </dt>
            <dd>{taxEntity.description}</dd>
            <dt>
              <span id="percentage">
                <Translate contentKey="luulposApp.tax.percentage">Percentage</Translate>
              </span>
            </dt>
            <dd>{taxEntity.percentage}</dd>
            <dt>
              <span id="amount">
                <Translate contentKey="luulposApp.tax.amount">Amount</Translate>
              </span>
            </dt>
            <dd>{taxEntity.amount}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="luulposApp.tax.active">Active</Translate>
              </span>
            </dt>
            <dd>{taxEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="luulposApp.tax.shop">Shop</Translate>
            </dt>
            <dd>{taxEntity.shopShopName ? taxEntity.shopShopName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/tax" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/tax/${taxEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ tax }: IRootState) => ({
  taxEntity: tax.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TaxDetail);
