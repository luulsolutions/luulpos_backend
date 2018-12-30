import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <DropdownItem tag={Link} to="/entity/company">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.company" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/shop">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.shop" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/shop-section">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.shopSection" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/section-table">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.sectionTable" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/system-events-history">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.systemEventsHistory" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/product">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.product" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/product-category">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.productCategory" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/product-variant">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.productVariant" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/product-extra">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.productExtra" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/product-type">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.productType" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/system-config">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.systemConfig" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/email-balancer">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.emailBalancer" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/profile">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.profile" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/employee-timesheet">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.employeeTimesheet" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/orders">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.orders" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/orders-line">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.ordersLine" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/orders-line-variant">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.ordersLineVariant" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/orders-line-extra">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.ordersLineExtra" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/discount">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.discount" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/tax">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.tax" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/payment-method">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.paymentMethod" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/suspension-history">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.suspensionHistory" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/shop-device">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.shopDevice" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/payment-method-config">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.paymentMethodConfig" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/payment">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.payment" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/shop-change">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.shopChange" />
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
