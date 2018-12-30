/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import OrdersLineExtraComponentsPage from './orders-line-extra.page-object';
import { OrdersLineExtraDeleteDialog } from './orders-line-extra.page-object';
import OrdersLineExtraUpdatePage from './orders-line-extra-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('OrdersLineExtra e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ordersLineExtraUpdatePage: OrdersLineExtraUpdatePage;
  let ordersLineExtraComponentsPage: OrdersLineExtraComponentsPage;
  let ordersLineExtraDeleteDialog: OrdersLineExtraDeleteDialog;
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

  it('should load OrdersLineExtras', async () => {
    await navBarPage.getEntityPage('orders-line-extra');
    ordersLineExtraComponentsPage = new OrdersLineExtraComponentsPage();
    expect(await ordersLineExtraComponentsPage.getTitle().getText()).to.match(/Orders Line Extras/);
  });

  it('should load create OrdersLineExtra page', async () => {
    await ordersLineExtraComponentsPage.clickOnCreateButton();
    ordersLineExtraUpdatePage = new OrdersLineExtraUpdatePage();
    expect(await ordersLineExtraUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.ordersLineExtra.home.createOrEditLabel/);
  });

  it('should create and save OrdersLineExtras', async () => {
    const nbButtonsBeforeCreate = await ordersLineExtraComponentsPage.countDeleteButtons();

    await ordersLineExtraUpdatePage.setOrdersLineExtraNameInput('ordersLineExtraName');
    expect(await ordersLineExtraUpdatePage.getOrdersLineExtraNameInput()).to.match(/ordersLineExtraName/);
    await ordersLineExtraUpdatePage.setOrdersLineExtraValueInput('ordersLineExtraValue');
    expect(await ordersLineExtraUpdatePage.getOrdersLineExtraValueInput()).to.match(/ordersLineExtraValue/);
    await ordersLineExtraUpdatePage.setOrdersLineExtraPriceInput('5');
    expect(await ordersLineExtraUpdatePage.getOrdersLineExtraPriceInput()).to.eq('5');
    await ordersLineExtraUpdatePage.setOrdersOptionDescriptionInput('ordersOptionDescription');
    expect(await ordersLineExtraUpdatePage.getOrdersOptionDescriptionInput()).to.match(/ordersOptionDescription/);
    await ordersLineExtraUpdatePage.setFullPhotoInput(absolutePath);
    await ordersLineExtraUpdatePage.setFullPhotoUrlInput('fullPhotoUrl');
    expect(await ordersLineExtraUpdatePage.getFullPhotoUrlInput()).to.match(/fullPhotoUrl/);
    await ordersLineExtraUpdatePage.setThumbnailPhotoInput(absolutePath);
    await ordersLineExtraUpdatePage.setThumbnailPhotoUrlInput('thumbnailPhotoUrl');
    expect(await ordersLineExtraUpdatePage.getThumbnailPhotoUrlInput()).to.match(/thumbnailPhotoUrl/);
    await ordersLineExtraUpdatePage.ordersLineVariantSelectLastOption();
    await waitUntilDisplayed(ordersLineExtraUpdatePage.getSaveButton());
    await ordersLineExtraUpdatePage.save();
    await waitUntilHidden(ordersLineExtraUpdatePage.getSaveButton());
    expect(await ordersLineExtraUpdatePage.getSaveButton().isPresent()).to.be.false;

    await ordersLineExtraComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await ordersLineExtraComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last OrdersLineExtra', async () => {
    await ordersLineExtraComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await ordersLineExtraComponentsPage.countDeleteButtons();
    await ordersLineExtraComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    ordersLineExtraDeleteDialog = new OrdersLineExtraDeleteDialog();
    expect(await ordersLineExtraDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.ordersLineExtra.delete.question/);
    await ordersLineExtraDeleteDialog.clickOnConfirmButton();

    await ordersLineExtraComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await ordersLineExtraComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
