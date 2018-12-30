/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ShopDeviceComponentsPage from './shop-device.page-object';
import { ShopDeviceDeleteDialog } from './shop-device.page-object';
import ShopDeviceUpdatePage from './shop-device-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('ShopDevice e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let shopDeviceUpdatePage: ShopDeviceUpdatePage;
  let shopDeviceComponentsPage: ShopDeviceComponentsPage;
  let shopDeviceDeleteDialog: ShopDeviceDeleteDialog;

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

  it('should load ShopDevices', async () => {
    await navBarPage.getEntityPage('shop-device');
    shopDeviceComponentsPage = new ShopDeviceComponentsPage();
    expect(await shopDeviceComponentsPage.getTitle().getText()).to.match(/Shop Devices/);
  });

  it('should load create ShopDevice page', async () => {
    await shopDeviceComponentsPage.clickOnCreateButton();
    shopDeviceUpdatePage = new ShopDeviceUpdatePage();
    expect(await shopDeviceUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.shopDevice.home.createOrEditLabel/);
  });

  it('should create and save ShopDevices', async () => {
    const nbButtonsBeforeCreate = await shopDeviceComponentsPage.countDeleteButtons();

    await shopDeviceUpdatePage.setDeviceNameInput('deviceName');
    expect(await shopDeviceUpdatePage.getDeviceNameInput()).to.match(/deviceName/);
    await shopDeviceUpdatePage.setDeviceModelInput('deviceModel');
    expect(await shopDeviceUpdatePage.getDeviceModelInput()).to.match(/deviceModel/);
    await shopDeviceUpdatePage.setRegisteredDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await shopDeviceUpdatePage.getRegisteredDateInput()).to.contain('2001-01-01T02:30');
    await shopDeviceUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(shopDeviceUpdatePage.getSaveButton());
    await shopDeviceUpdatePage.save();
    await waitUntilHidden(shopDeviceUpdatePage.getSaveButton());
    expect(await shopDeviceUpdatePage.getSaveButton().isPresent()).to.be.false;

    await shopDeviceComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await shopDeviceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ShopDevice', async () => {
    await shopDeviceComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await shopDeviceComponentsPage.countDeleteButtons();
    await shopDeviceComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    shopDeviceDeleteDialog = new ShopDeviceDeleteDialog();
    expect(await shopDeviceDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.shopDevice.delete.question/);
    await shopDeviceDeleteDialog.clickOnConfirmButton();

    await shopDeviceComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await shopDeviceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
