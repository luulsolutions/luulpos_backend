import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shop.reducer';
import { IShop } from 'app/shared/model/shop.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShopDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShopDetail extends React.Component<IShopDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shopEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="luulposApp.shop.detail.title">Shop</Translate> [<b>{shopEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="shopName">
                <Translate contentKey="luulposApp.shop.shopName">Shop Name</Translate>
              </span>
            </dt>
            <dd>{shopEntity.shopName}</dd>
            <dt>
              <span id="shopAccountType">
                <Translate contentKey="luulposApp.shop.shopAccountType">Shop Account Type</Translate>
              </span>
            </dt>
            <dd>{shopEntity.shopAccountType}</dd>
            <dt>
              <span id="approvalDate">
                <Translate contentKey="luulposApp.shop.approvalDate">Approval Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={shopEntity.approvalDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="address">
                <Translate contentKey="luulposApp.shop.address">Address</Translate>
              </span>
            </dt>
            <dd>{shopEntity.address}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="luulposApp.shop.email">Email</Translate>
              </span>
            </dt>
            <dd>{shopEntity.email}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="luulposApp.shop.description">Description</Translate>
              </span>
            </dt>
            <dd>{shopEntity.description}</dd>
            <dt>
              <span id="note">
                <Translate contentKey="luulposApp.shop.note">Note</Translate>
              </span>
            </dt>
            <dd>{shopEntity.note}</dd>
            <dt>
              <span id="landland">
                <Translate contentKey="luulposApp.shop.landland">Landland</Translate>
              </span>
            </dt>
            <dd>{shopEntity.landland}</dd>
            <dt>
              <span id="mobile">
                <Translate contentKey="luulposApp.shop.mobile">Mobile</Translate>
              </span>
            </dt>
            <dd>{shopEntity.mobile}</dd>
            <dt>
              <span id="createdDate">
                <Translate contentKey="luulposApp.shop.createdDate">Created Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={shopEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="shopLogo">
                <Translate contentKey="luulposApp.shop.shopLogo">Shop Logo</Translate>
              </span>
            </dt>
            <dd>
              {shopEntity.shopLogo ? (
                <div>
                  <a onClick={openFile(shopEntity.shopLogoContentType, shopEntity.shopLogo)}>
                    <img src={`data:${shopEntity.shopLogoContentType};base64,${shopEntity.shopLogo}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {shopEntity.shopLogoContentType}, {byteSize(shopEntity.shopLogo)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="shopLogoUrl">
                <Translate contentKey="luulposApp.shop.shopLogoUrl">Shop Logo Url</Translate>
              </span>
            </dt>
            <dd>{shopEntity.shopLogoUrl}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="luulposApp.shop.active">Active</Translate>
              </span>
            </dt>
            <dd>{shopEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <span id="currency">
                <Translate contentKey="luulposApp.shop.currency">Currency</Translate>
              </span>
            </dt>
            <dd>{shopEntity.currency}</dd>
            <dt>
              <Translate contentKey="luulposApp.shop.company">Company</Translate>
            </dt>
            <dd>{shopEntity.companyCompanyName ? shopEntity.companyCompanyName : ''}</dd>
            <dt>
              <Translate contentKey="luulposApp.shop.approvedBy">Approved By</Translate>
            </dt>
            <dd>{shopEntity.approvedByFirstName ? shopEntity.approvedByFirstName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/shop" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/shop/${shopEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ shop }: IRootState) => ({
  shopEntity: shop.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShopDetail);
