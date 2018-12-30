/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import PaymentMethodComponentsPage from './payment-method.page-object';
import { PaymentMethodDeleteDialog } from './payment-method.page-object';
import PaymentMethodUpdatePage from './payment-method-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('PaymentMethod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentMethodUpdatePage: PaymentMethodUpdatePage;
  let paymentMethodComponentsPage: PaymentMethodComponentsPage;
  let paymentMethodDeleteDialog: PaymentMethodDeleteDialog;

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

  it('should load PaymentMethods', async () => {
    await navBarPage.getEntityPage('payment-method');
    paymentMethodComponentsPage = new PaymentMethodComponentsPage();
    expect(await paymentMethodComponentsPage.getTitle().getText()).to.match(/Payment Methods/);
  });

  it('should load create PaymentMethod page', async () => {
    await paymentMethodComponentsPage.clickOnCreateButton();
    paymentMethodUpdatePage = new PaymentMethodUpdatePage();
    expect(await paymentMethodUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.paymentMethod.home.createOrEditLabel/);
  });

  it('should create and save PaymentMethods', async () => {
    const nbButtonsBeforeCreate = await paymentMethodComponentsPage.countDeleteButtons();

    await paymentMethodUpdatePage.setPaymentMethodInput('paymentMethod');
    expect(await paymentMethodUpdatePage.getPaymentMethodInput()).to.match(/paymentMethod/);
    await paymentMethodUpdatePage.setDescriptionInput('description');
    expect(await paymentMethodUpdatePage.getDescriptionInput()).to.match(/description/);
    const selectedActive = await paymentMethodUpdatePage.getActiveInput().isSelected();
    if (selectedActive) {
      await paymentMethodUpdatePage.getActiveInput().click();
      expect(await paymentMethodUpdatePage.getActiveInput().isSelected()).to.be.false;
    } else {
      await paymentMethodUpdatePage.getActiveInput().click();
      expect(await paymentMethodUpdatePage.getActiveInput().isSelected()).to.be.true;
    }
    await paymentMethodUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(paymentMethodUpdatePage.getSaveButton());
    await paymentMethodUpdatePage.save();
    await waitUntilHidden(paymentMethodUpdatePage.getSaveButton());
    expect(await paymentMethodUpdatePage.getSaveButton().isPresent()).to.be.false;

    await paymentMethodComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await paymentMethodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last PaymentMethod', async () => {
    await paymentMethodComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await paymentMethodComponentsPage.countDeleteButtons();
    await paymentMethodComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    paymentMethodDeleteDialog = new PaymentMethodDeleteDialog();
    expect(await paymentMethodDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.paymentMethod.delete.question/);
    await paymentMethodDeleteDialog.clickOnConfirmButton();

    await paymentMethodComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await paymentMethodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
