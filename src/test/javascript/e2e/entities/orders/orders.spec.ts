/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import OrdersComponentsPage from './orders.page-object';
import { OrdersDeleteDialog } from './orders.page-object';
import OrdersUpdatePage from './orders-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Orders e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ordersUpdatePage: OrdersUpdatePage;
  let ordersComponentsPage: OrdersComponentsPage;
  let ordersDeleteDialog: OrdersDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();

    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Orders', async () => {
    await navBarPage.getEntityPage('orders');
    ordersComponentsPage = new OrdersComponentsPage();
    expect(await ordersComponentsPage.getTitle().getText()).to.match(/Orders/);
  });

  it('should load create Orders page', async () => {
    await ordersComponentsPage.clickOnCreateButton();
    ordersUpdatePage = new OrdersUpdatePage();
    expect(await ordersUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.orders.home.createOrEditLabel/);
  });

  it('should create and save Orders', async () => {
    const nbButtonsBeforeCreate = await ordersComponentsPage.countDeleteButtons();

    await ordersUpdatePage.setDescriptionInput('description');
    expect(await ordersUpdatePage.getDescriptionInput()).to.match(/description/);
    await ordersUpdatePage.setCustomerNameInput('customerName');
    expect(await ordersUpdatePage.getCustomerNameInput()).to.match(/customerName/);
    await ordersUpdatePage.setTotalPriceInput('5');
    expect(await ordersUpdatePage.getTotalPriceInput()).to.eq('5');
    await ordersUpdatePage.setQuantityInput('5');
    expect(await ordersUpdatePage.getQuantityInput()).to.eq('5');
    await ordersUpdatePage.setDiscountPercentageInput('5');
    expect(await ordersUpdatePage.getDiscountPercentageInput()).to.eq('5');
    await ordersUpdatePage.setDiscountAmountInput('5');
    expect(await ordersUpdatePage.getDiscountAmountInput()).to.eq('5');
    await ordersUpdatePage.setTaxPercentageInput('5');
    expect(await ordersUpdatePage.getTaxPercentageInput()).to.eq('5');
    await ordersUpdatePage.setTaxAmountInput('5');
    expect(await ordersUpdatePage.getTaxAmountInput()).to.eq('5');
    await ordersUpdatePage.orderStatusSelectLastOption();
    await ordersUpdatePage.setNoteInput('note');
    expect(await ordersUpdatePage.getNoteInput()).to.match(/note/);
    await ordersUpdatePage.setOrderDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await ordersUpdatePage.getOrderDateInput()).to.contain('2001-01-01T02:30');
    const selectedIsVariantOrder = await ordersUpdatePage.getIsVariantOrderInput().isSelected();
    if (selectedIsVariantOrder) {
      await ordersUpdatePage.getIsVariantOrderInput().click();
      expect(await ordersUpdatePage.getIsVariantOrderInput().isSelected()).to.be.false;
    } else {
      await ordersUpdatePage.getIsVariantOrderInput().click();
      expect(await ordersUpdatePage.getIsVariantOrderInput().isSelected()).to.be.true;
    }
    await ordersUpdatePage.paymentMethodSelectLastOption();
    await ordersUpdatePage.soldBySelectLastOption();
    await ordersUpdatePage.preparedBySelectLastOption();
    await ordersUpdatePage.shopDeviceSelectLastOption();
    await ordersUpdatePage.sectionTableSelectLastOption();
    await ordersUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(ordersUpdatePage.getSaveButton());
    await ordersUpdatePage.save();
    await waitUntilHidden(ordersUpdatePage.getSaveButton());
    expect(await ordersUpdatePage.getSaveButton().isPresent()).to.be.false;

    await ordersComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await ordersComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Orders', async () => {
    await ordersComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await ordersComponentsPage.countDeleteButtons();
    await ordersComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    ordersDeleteDialog = new OrdersDeleteDialog();
    expect(await ordersDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.orders.delete.question/);
    await ordersDeleteDialog.clickOnConfirmButton();

    await ordersComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await ordersComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
