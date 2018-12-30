/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import TaxComponentsPage from './tax.page-object';
import { TaxDeleteDialog } from './tax.page-object';
import TaxUpdatePage from './tax-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Tax e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let taxUpdatePage: TaxUpdatePage;
  let taxComponentsPage: TaxComponentsPage;
  let taxDeleteDialog: TaxDeleteDialog;

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

  it('should load Taxes', async () => {
    await navBarPage.getEntityPage('tax');
    taxComponentsPage = new TaxComponentsPage();
    expect(await taxComponentsPage.getTitle().getText()).to.match(/Taxes/);
  });

  it('should load create Tax page', async () => {
    await taxComponentsPage.clickOnCreateButton();
    taxUpdatePage = new TaxUpdatePage();
    expect(await taxUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.tax.home.createOrEditLabel/);
  });

  it('should create and save Taxes', async () => {
    const nbButtonsBeforeCreate = await taxComponentsPage.countDeleteButtons();

    await taxUpdatePage.setDescriptionInput('description');
    expect(await taxUpdatePage.getDescriptionInput()).to.match(/description/);
    await taxUpdatePage.setPercentageInput('5');
    expect(await taxUpdatePage.getPercentageInput()).to.eq('5');
    await taxUpdatePage.setAmountInput('5');
    expect(await taxUpdatePage.getAmountInput()).to.eq('5');
    const selectedActive = await taxUpdatePage.getActiveInput().isSelected();
    if (selectedActive) {
      await taxUpdatePage.getActiveInput().click();
      expect(await taxUpdatePage.getActiveInput().isSelected()).to.be.false;
    } else {
      await taxUpdatePage.getActiveInput().click();
      expect(await taxUpdatePage.getActiveInput().isSelected()).to.be.true;
    }
    await taxUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(taxUpdatePage.getSaveButton());
    await taxUpdatePage.save();
    await waitUntilHidden(taxUpdatePage.getSaveButton());
    expect(await taxUpdatePage.getSaveButton().isPresent()).to.be.false;

    await taxComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await taxComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Tax', async () => {
    await taxComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await taxComponentsPage.countDeleteButtons();
    await taxComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    taxDeleteDialog = new TaxDeleteDialog();
    expect(await taxDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.tax.delete.question/);
    await taxDeleteDialog.clickOnConfirmButton();

    await taxComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await taxComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
