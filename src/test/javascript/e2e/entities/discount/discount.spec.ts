/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import DiscountComponentsPage from './discount.page-object';
import { DiscountDeleteDialog } from './discount.page-object';
import DiscountUpdatePage from './discount-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Discount e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let discountUpdatePage: DiscountUpdatePage;
  let discountComponentsPage: DiscountComponentsPage;
  let discountDeleteDialog: DiscountDeleteDialog;

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

  it('should load Discounts', async () => {
    await navBarPage.getEntityPage('discount');
    discountComponentsPage = new DiscountComponentsPage();
    expect(await discountComponentsPage.getTitle().getText()).to.match(/Discounts/);
  });

  it('should load create Discount page', async () => {
    await discountComponentsPage.clickOnCreateButton();
    discountUpdatePage = new DiscountUpdatePage();
    expect(await discountUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.discount.home.createOrEditLabel/);
  });

  it('should create and save Discounts', async () => {
    const nbButtonsBeforeCreate = await discountComponentsPage.countDeleteButtons();

    await discountUpdatePage.setDescriptionInput('description');
    expect(await discountUpdatePage.getDescriptionInput()).to.match(/description/);
    await discountUpdatePage.setPercentageInput('5');
    expect(await discountUpdatePage.getPercentageInput()).to.eq('5');
    await discountUpdatePage.setAmountInput('5');
    expect(await discountUpdatePage.getAmountInput()).to.eq('5');
    const selectedActive = await discountUpdatePage.getActiveInput().isSelected();
    if (selectedActive) {
      await discountUpdatePage.getActiveInput().click();
      expect(await discountUpdatePage.getActiveInput().isSelected()).to.be.false;
    } else {
      await discountUpdatePage.getActiveInput().click();
      expect(await discountUpdatePage.getActiveInput().isSelected()).to.be.true;
    }
    await discountUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(discountUpdatePage.getSaveButton());
    await discountUpdatePage.save();
    await waitUntilHidden(discountUpdatePage.getSaveButton());
    expect(await discountUpdatePage.getSaveButton().isPresent()).to.be.false;

    await discountComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await discountComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Discount', async () => {
    await discountComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await discountComponentsPage.countDeleteButtons();
    await discountComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    discountDeleteDialog = new DiscountDeleteDialog();
    expect(await discountDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.discount.delete.question/);
    await discountDeleteDialog.clickOnConfirmButton();

    await discountComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await discountComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
