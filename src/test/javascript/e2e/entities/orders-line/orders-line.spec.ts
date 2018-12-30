/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import OrdersLineComponentsPage from './orders-line.page-object';
import { OrdersLineDeleteDialog } from './orders-line.page-object';
import OrdersLineUpdatePage from './orders-line-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('OrdersLine e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ordersLineUpdatePage: OrdersLineUpdatePage;
  let ordersLineComponentsPage: OrdersLineComponentsPage;
  let ordersLineDeleteDialog: OrdersLineDeleteDialog;
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

  it('should load OrdersLines', async () => {
    await navBarPage.getEntityPage('orders-line');
    ordersLineComponentsPage = new OrdersLineComponentsPage();
    expect(await ordersLineComponentsPage.getTitle().getText()).to.match(/Orders Lines/);
  });

  it('should load create OrdersLine page', async () => {
    await ordersLineComponentsPage.clickOnCreateButton();
    ordersLineUpdatePage = new OrdersLineUpdatePage();
    expect(await ordersLineUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.ordersLine.home.createOrEditLabel/);
  });

  it('should create and save OrdersLines', async () => {
    const nbButtonsBeforeCreate = await ordersLineComponentsPage.countDeleteButtons();

    await ordersLineUpdatePage.setOrdersLineNameInput('ordersLineName');
    expect(await ordersLineUpdatePage.getOrdersLineNameInput()).to.match(/ordersLineName/);
    await ordersLineUpdatePage.setOrdersLineValueInput('ordersLineValue');
    expect(await ordersLineUpdatePage.getOrdersLineValueInput()).to.match(/ordersLineValue/);
    await ordersLineUpdatePage.setOrdersLinePriceInput('5');
    expect(await ordersLineUpdatePage.getOrdersLinePriceInput()).to.eq('5');
    await ordersLineUpdatePage.setOrdersLineDescriptionInput('ordersLineDescription');
    expect(await ordersLineUpdatePage.getOrdersLineDescriptionInput()).to.match(/ordersLineDescription/);
    await ordersLineUpdatePage.setThumbnailPhotoInput(absolutePath);
    await ordersLineUpdatePage.setFullPhotoInput(absolutePath);
    await ordersLineUpdatePage.setFullPhotoUrlInput('fullPhotoUrl');
    expect(await ordersLineUpdatePage.getFullPhotoUrlInput()).to.match(/fullPhotoUrl/);
    await ordersLineUpdatePage.setThumbnailPhotoUrlInput('thumbnailPhotoUrl');
    expect(await ordersLineUpdatePage.getThumbnailPhotoUrlInput()).to.match(/thumbnailPhotoUrl/);
    await ordersLineUpdatePage.ordersSelectLastOption();
    await ordersLineUpdatePage.productSelectLastOption();
    await waitUntilDisplayed(ordersLineUpdatePage.getSaveButton());
    await ordersLineUpdatePage.save();
    await waitUntilHidden(ordersLineUpdatePage.getSaveButton());
    expect(await ordersLineUpdatePage.getSaveButton().isPresent()).to.be.false;

    await ordersLineComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await ordersLineComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last OrdersLine', async () => {
    await ordersLineComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await ordersLineComponentsPage.countDeleteButtons();
    await ordersLineComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    ordersLineDeleteDialog = new OrdersLineDeleteDialog();
    expect(await ordersLineDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.ordersLine.delete.question/);
    await ordersLineDeleteDialog.clickOnConfirmButton();

    await ordersLineComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await ordersLineComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
