/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ShopSectionComponentsPage from './shop-section.page-object';
import { ShopSectionDeleteDialog } from './shop-section.page-object';
import ShopSectionUpdatePage from './shop-section-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('ShopSection e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let shopSectionUpdatePage: ShopSectionUpdatePage;
  let shopSectionComponentsPage: ShopSectionComponentsPage;
  let shopSectionDeleteDialog: ShopSectionDeleteDialog;

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

  it('should load ShopSections', async () => {
    await navBarPage.getEntityPage('shop-section');
    shopSectionComponentsPage = new ShopSectionComponentsPage();
    expect(await shopSectionComponentsPage.getTitle().getText()).to.match(/Shop Sections/);
  });

  it('should load create ShopSection page', async () => {
    await shopSectionComponentsPage.clickOnCreateButton();
    shopSectionUpdatePage = new ShopSectionUpdatePage();
    expect(await shopSectionUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.shopSection.home.createOrEditLabel/);
  });

  it('should create and save ShopSections', async () => {
    const nbButtonsBeforeCreate = await shopSectionComponentsPage.countDeleteButtons();

    await shopSectionUpdatePage.setSectionNameInput('sectionName');
    expect(await shopSectionUpdatePage.getSectionNameInput()).to.match(/sectionName/);
    await shopSectionUpdatePage.setDescriptionInput('description');
    expect(await shopSectionUpdatePage.getDescriptionInput()).to.match(/description/);
    await shopSectionUpdatePage.setSurchargePercentageInput('5');
    expect(await shopSectionUpdatePage.getSurchargePercentageInput()).to.eq('5');
    await shopSectionUpdatePage.setSurchargeFlatAmountInput('5');
    expect(await shopSectionUpdatePage.getSurchargeFlatAmountInput()).to.eq('5');
    const selectedUsePercentage = await shopSectionUpdatePage.getUsePercentageInput().isSelected();
    if (selectedUsePercentage) {
      await shopSectionUpdatePage.getUsePercentageInput().click();
      expect(await shopSectionUpdatePage.getUsePercentageInput().isSelected()).to.be.false;
    } else {
      await shopSectionUpdatePage.getUsePercentageInput().click();
      expect(await shopSectionUpdatePage.getUsePercentageInput().isSelected()).to.be.true;
    }
    await shopSectionUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(shopSectionUpdatePage.getSaveButton());
    await shopSectionUpdatePage.save();
    await waitUntilHidden(shopSectionUpdatePage.getSaveButton());
    expect(await shopSectionUpdatePage.getSaveButton().isPresent()).to.be.false;

    await shopSectionComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await shopSectionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ShopSection', async () => {
    await shopSectionComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await shopSectionComponentsPage.countDeleteButtons();
    await shopSectionComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    shopSectionDeleteDialog = new ShopSectionDeleteDialog();
    expect(await shopSectionDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.shopSection.delete.question/);
    await shopSectionDeleteDialog.clickOnConfirmButton();

    await shopSectionComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await shopSectionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
