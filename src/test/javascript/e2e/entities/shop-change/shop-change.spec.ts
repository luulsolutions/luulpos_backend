/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ShopChangeComponentsPage from './shop-change.page-object';
import { ShopChangeDeleteDialog } from './shop-change.page-object';
import ShopChangeUpdatePage from './shop-change-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('ShopChange e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let shopChangeUpdatePage: ShopChangeUpdatePage;
  let shopChangeComponentsPage: ShopChangeComponentsPage;
  let shopChangeDeleteDialog: ShopChangeDeleteDialog;

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

  it('should load ShopChanges', async () => {
    await navBarPage.getEntityPage('shop-change');
    shopChangeComponentsPage = new ShopChangeComponentsPage();
    expect(await shopChangeComponentsPage.getTitle().getText()).to.match(/Shop Changes/);
  });

  it('should load create ShopChange page', async () => {
    await shopChangeComponentsPage.clickOnCreateButton();
    shopChangeUpdatePage = new ShopChangeUpdatePage();
    expect(await shopChangeUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.shopChange.home.createOrEditLabel/);
  });

  it('should create and save ShopChanges', async () => {
    const nbButtonsBeforeCreate = await shopChangeComponentsPage.countDeleteButtons();

    await shopChangeUpdatePage.setChangeInput('change');
    expect(await shopChangeUpdatePage.getChangeInput()).to.match(/change/);
    await shopChangeUpdatePage.setChangedEntityInput('changedEntity');
    expect(await shopChangeUpdatePage.getChangedEntityInput()).to.match(/changedEntity/);
    await shopChangeUpdatePage.setNoteInput('note');
    expect(await shopChangeUpdatePage.getNoteInput()).to.match(/note/);
    await shopChangeUpdatePage.setChangeDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await shopChangeUpdatePage.getChangeDateInput()).to.contain('2001-01-01T02:30');
    await shopChangeUpdatePage.shopSelectLastOption();
    await shopChangeUpdatePage.changedBySelectLastOption();
    await waitUntilDisplayed(shopChangeUpdatePage.getSaveButton());
    await shopChangeUpdatePage.save();
    await waitUntilHidden(shopChangeUpdatePage.getSaveButton());
    expect(await shopChangeUpdatePage.getSaveButton().isPresent()).to.be.false;

    await shopChangeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await shopChangeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ShopChange', async () => {
    await shopChangeComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await shopChangeComponentsPage.countDeleteButtons();
    await shopChangeComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    shopChangeDeleteDialog = new ShopChangeDeleteDialog();
    expect(await shopChangeDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.shopChange.delete.question/);
    await shopChangeDeleteDialog.clickOnConfirmButton();

    await shopChangeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await shopChangeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
