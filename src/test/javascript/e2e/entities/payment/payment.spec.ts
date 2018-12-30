/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import PaymentComponentsPage from './payment.page-object';
import { PaymentDeleteDialog } from './payment.page-object';
import PaymentUpdatePage from './payment-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Payment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentUpdatePage: PaymentUpdatePage;
  let paymentComponentsPage: PaymentComponentsPage;
  let paymentDeleteDialog: PaymentDeleteDialog;

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

  it('should load Payments', async () => {
    await navBarPage.getEntityPage('payment');
    paymentComponentsPage = new PaymentComponentsPage();
    expect(await paymentComponentsPage.getTitle().getText()).to.match(/Payments/);
  });

  it('should load create Payment page', async () => {
    await paymentComponentsPage.clickOnCreateButton();
    paymentUpdatePage = new PaymentUpdatePage();
    expect(await paymentUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.payment.home.createOrEditLabel/);
  });

  it('should create and save Payments', async () => {
    const nbButtonsBeforeCreate = await paymentComponentsPage.countDeleteButtons();

    await paymentUpdatePage.setPaymentDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await paymentUpdatePage.getPaymentDateInput()).to.contain('2001-01-01T02:30');
    await paymentUpdatePage.setPaymentProviderInput('paymentProvider');
    expect(await paymentUpdatePage.getPaymentProviderInput()).to.match(/paymentProvider/);
    await paymentUpdatePage.setAmountInput('5');
    expect(await paymentUpdatePage.getAmountInput()).to.eq('5');
    await paymentUpdatePage.paymentStatusSelectLastOption();
    await paymentUpdatePage.setCurencyInput('curency');
    expect(await paymentUpdatePage.getCurencyInput()).to.match(/curency/);
    await paymentUpdatePage.setCustomerNameInput('customerName');
    expect(await paymentUpdatePage.getCustomerNameInput()).to.match(/customerName/);
    await paymentUpdatePage.shopSelectLastOption();
    await paymentUpdatePage.processedBySelectLastOption();
    await paymentUpdatePage.paymentMethodSelectLastOption();
    await paymentUpdatePage.orderSelectLastOption();
    await waitUntilDisplayed(paymentUpdatePage.getSaveButton());
    await paymentUpdatePage.save();
    await waitUntilHidden(paymentUpdatePage.getSaveButton());
    expect(await paymentUpdatePage.getSaveButton().isPresent()).to.be.false;

    await paymentComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await paymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Payment', async () => {
    await paymentComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await paymentComponentsPage.countDeleteButtons();
    await paymentComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    paymentDeleteDialog = new PaymentDeleteDialog();
    expect(await paymentDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.payment.delete.question/);
    await paymentDeleteDialog.clickOnConfirmButton();

    await paymentComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await paymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
