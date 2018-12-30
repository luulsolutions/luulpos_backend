/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ShopComponentsPage from './shop.page-object';
import { ShopDeleteDialog } from './shop.page-object';
import ShopUpdatePage from './shop-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('Shop e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let shopUpdatePage: ShopUpdatePage;
  let shopComponentsPage: ShopComponentsPage;
  let shopDeleteDialog: ShopDeleteDialog;
  const fileToUpload = '../../../../../main/webapp/static/images/logo-jhipster.png';
  const absolutePath = path.resolve(__dirname, fileToUpload);

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

  it('should load Shops', async () => {
    await navBarPage.getEntityPage('shop');
    shopComponentsPage = new ShopComponentsPage();
    expect(await shopComponentsPage.getTitle().getText()).to.match(/Shops/);
  });

  it('should load create Shop page', async () => {
    await shopComponentsPage.clickOnCreateButton();
    shopUpdatePage = new ShopUpdatePage();
    expect(await shopUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.shop.home.createOrEditLabel/);
  });

  it('should create and save Shops', async () => {
    const nbButtonsBeforeCreate = await shopComponentsPage.countDeleteButtons();

    await shopUpdatePage.setShopNameInput('shopName');
    expect(await shopUpdatePage.getShopNameInput()).to.match(/shopName/);
    await shopUpdatePage.shopAccountTypeSelectLastOption();
    await shopUpdatePage.setApprovalDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await shopUpdatePage.getApprovalDateInput()).to.contain('2001-01-01T02:30');
    await shopUpdatePage.setAddressInput('address');
    expect(await shopUpdatePage.getAddressInput()).to.match(/address/);
    await shopUpdatePage.setEmailInput('email');
    expect(await shopUpdatePage.getEmailInput()).to.match(/email/);
    await shopUpdatePage.setDescriptionInput('description');
    expect(await shopUpdatePage.getDescriptionInput()).to.match(/description/);
    await shopUpdatePage.setNoteInput('note');
    expect(await shopUpdatePage.getNoteInput()).to.match(/note/);
    await shopUpdatePage.setLandlandInput('landland');
    expect(await shopUpdatePage.getLandlandInput()).to.match(/landland/);
    await shopUpdatePage.setMobileInput('mobile');
    expect(await shopUpdatePage.getMobileInput()).to.match(/mobile/);
    await shopUpdatePage.setCreatedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await shopUpdatePage.getCreatedDateInput()).to.contain('2001-01-01T02:30');
    await shopUpdatePage.setShopLogoInput(absolutePath);
    await shopUpdatePage.setShopLogoUrlInput('shopLogoUrl');
    expect(await shopUpdatePage.getShopLogoUrlInput()).to.match(/shopLogoUrl/);
    const selectedActive = await shopUpdatePage.getActiveInput().isSelected();
    if (selectedActive) {
      await shopUpdatePage.getActiveInput().click();
      expect(await shopUpdatePage.getActiveInput().isSelected()).to.be.false;
    } else {
      await shopUpdatePage.getActiveInput().click();
      expect(await shopUpdatePage.getActiveInput().isSelected()).to.be.true;
    }
    await shopUpdatePage.setCurrencyInput('currency');
    expect(await shopUpdatePage.getCurrencyInput()).to.match(/currency/);
    await shopUpdatePage.companySelectLastOption();
    await shopUpdatePage.approvedBySelectLastOption();
    await waitUntilDisplayed(shopUpdatePage.getSaveButton());
    await shopUpdatePage.save();
    await waitUntilHidden(shopUpdatePage.getSaveButton());
    expect(await shopUpdatePage.getSaveButton().isPresent()).to.be.false;

    await shopComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await shopComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Shop', async () => {
    await shopComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await shopComponentsPage.countDeleteButtons();
    await shopComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    shopDeleteDialog = new ShopDeleteDialog();
    expect(await shopDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.shop.delete.question/);
    await shopDeleteDialog.clickOnConfirmButton();

    await shopComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await shopComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
