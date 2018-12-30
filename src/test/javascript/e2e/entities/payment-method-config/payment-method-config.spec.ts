/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import PaymentMethodConfigComponentsPage from './payment-method-config.page-object';
import { PaymentMethodConfigDeleteDialog } from './payment-method-config.page-object';
import PaymentMethodConfigUpdatePage from './payment-method-config-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('PaymentMethodConfig e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentMethodConfigUpdatePage: PaymentMethodConfigUpdatePage;
  let paymentMethodConfigComponentsPage: PaymentMethodConfigComponentsPage;
  let paymentMethodConfigDeleteDialog: PaymentMethodConfigDeleteDialog;

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

  it('should load PaymentMethodConfigs', async () => {
    await navBarPage.getEntityPage('payment-method-config');
    paymentMethodConfigComponentsPage = new PaymentMethodConfigComponentsPage();
    expect(await paymentMethodConfigComponentsPage.getTitle().getText()).to.match(/Payment Method Configs/);
  });

  it('should load create PaymentMethodConfig page', async () => {
    await paymentMethodConfigComponentsPage.clickOnCreateButton();
    paymentMethodConfigUpdatePage = new PaymentMethodConfigUpdatePage();
    expect(await paymentMethodConfigUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /luulposApp.paymentMethodConfig.home.createOrEditLabel/
    );
  });

  it('should create and save PaymentMethodConfigs', async () => {
    const nbButtonsBeforeCreate = await paymentMethodConfigComponentsPage.countDeleteButtons();

    await paymentMethodConfigUpdatePage.setKeyInput('key');
    expect(await paymentMethodConfigUpdatePage.getKeyInput()).to.match(/key/);
    await paymentMethodConfigUpdatePage.setValueInput('value');
    expect(await paymentMethodConfigUpdatePage.getValueInput()).to.match(/value/);
    await paymentMethodConfigUpdatePage.setNoteInput('note');
    expect(await paymentMethodConfigUpdatePage.getNoteInput()).to.match(/note/);
    const selectedEnabled = await paymentMethodConfigUpdatePage.getEnabledInput().isSelected();
    if (selectedEnabled) {
      await paymentMethodConfigUpdatePage.getEnabledInput().click();
      expect(await paymentMethodConfigUpdatePage.getEnabledInput().isSelected()).to.be.false;
    } else {
      await paymentMethodConfigUpdatePage.getEnabledInput().click();
      expect(await paymentMethodConfigUpdatePage.getEnabledInput().isSelected()).to.be.true;
    }
    await paymentMethodConfigUpdatePage.paymentMethodSelectLastOption();
    await waitUntilDisplayed(paymentMethodConfigUpdatePage.getSaveButton());
    await paymentMethodConfigUpdatePage.save();
    await waitUntilHidden(paymentMethodConfigUpdatePage.getSaveButton());
    expect(await paymentMethodConfigUpdatePage.getSaveButton().isPresent()).to.be.false;

    await paymentMethodConfigComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await paymentMethodConfigComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last PaymentMethodConfig', async () => {
    await paymentMethodConfigComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await paymentMethodConfigComponentsPage.countDeleteButtons();
    await paymentMethodConfigComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    paymentMethodConfigDeleteDialog = new PaymentMethodConfigDeleteDialog();
    expect(await paymentMethodConfigDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /luulposApp.paymentMethodConfig.delete.question/
    );
    await paymentMethodConfigDeleteDialog.clickOnConfirmButton();

    await paymentMethodConfigComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await paymentMethodConfigComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
