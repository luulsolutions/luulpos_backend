import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop-change.reducer';
import { IShopChange } from 'app/shared/model/shop-change.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopChangeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShopChangeDetail extends React.Component<IShopChangeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shopChangeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.shopChange.detail.title">ShopChange</Translate> [<b>{shopChangeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="change">
                <Translate contentKey="luulposApp.shopChange.change">Change</Translate>
              </span>
            </dt>
            <dd>{shopChangeEntity.change}</dd>
            <dt>
              <span id="changedEntity">
                <Translate contentKey="luulposApp.shopChange.changedEntity">Changed Entity</Translate>
              </span>
            </dt>
            <dd>{shopChangeEntity.changedEntity}</dd>
            <dt>
              <span id="note">
                <Translate contentKey="luulposApp.shopChange.note">Note</Translate>
              </span>
            </dt>
            <dd>{shopChangeEntity.note}</dd>
            <dt>
              <span id="changeDate">
                <Translate contentKey="luulposApp.shopChange.changeDate">Change Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={shopChangeEntity.changeDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="luulposApp.shopChange.shop">Shop</Translate>
            </dt>
            <dd>{shopChangeEntity.shopShopName ? shopChangeEntity.shopShopName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.shopChange.changedBy">Changed By</Translate>
            </dt>
            <dd>{shopChangeEntity.changedByFirstName ? shopChangeEntity.changedByFirstName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/shop-change" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/shop-change/${shopChangeEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ shopChange }: IRootState) => ({
  shopChangeEntity: shopChange.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShopChangeDetail);
