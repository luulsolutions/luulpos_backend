/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import OrdersLineVariantComponentsPage from './orders-line-variant.page-object';
import { OrdersLineVariantDeleteDialog } from './orders-line-variant.page-object';
import OrdersLineVariantUpdatePage from './orders-line-variant-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('OrdersLineVariant e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ordersLineVariantUpdatePage: OrdersLineVariantUpdatePage;
  let ordersLineVariantComponentsPage: OrdersLineVariantComponentsPage;
  let ordersLineVariantDeleteDialog: OrdersLineVariantDeleteDialog;
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

  it('should load OrdersLineVariants', async () => {
    await navBarPage.getEntityPage('orders-line-variant');
    ordersLineVariantComponentsPage = new OrdersLineVariantComponentsPage();
    expect(await ordersLineVariantComponentsPage.getTitle().getText()).to.match(/Orders Line Variants/);
  });

  it('should load create OrdersLineVariant page', async () => {
    await ordersLineVariantComponentsPage.clickOnCreateButton();
    ordersLineVariantUpdatePage = new OrdersLineVariantUpdatePage();
    expect(await ordersLineVariantUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /luulposApp.ordersLineVariant.home.createOrEditLabel/
    );
  });

  it('should create and save OrdersLineVariants', async () => {
    const nbButtonsBeforeCreate = await ordersLineVariantComponentsPage.countDeleteButtons();

    await ordersLineVariantUpdatePage.setVariantNameInput('variantName');
    expect(await ordersLineVariantUpdatePage.getVariantNameInput()).to.match(/variantName/);
    await ordersLineVariantUpdatePage.setVariantValueInput('variantValue');
    expect(await ordersLineVariantUpdatePage.getVariantValueInput()).to.match(/variantValue/);
    await ordersLineVariantUpdatePage.setDescriptionInput('description');
    expect(await ordersLineVariantUpdatePage.getDescriptionInput()).to.match(/description/);
    await ordersLineVariantUpdatePage.setPercentageInput('5');
    expect(await ordersLineVariantUpdatePage.getPercentageInput()).to.eq('5');
    await ordersLineVariantUpdatePage.setFullPhotoInput(absolutePath);
    await ordersLineVariantUpdatePage.setFullPhotoUrlInput('fullPhotoUrl');
    expect(await ordersLineVariantUpdatePage.getFullPhotoUrlInput()).to.match(/fullPhotoUrl/);
    await ordersLineVariantUpdatePage.setThumbnailPhotoInput(absolutePath);
    await ordersLineVariantUpdatePage.setThumbnailPhotoUrlInput('thumbnailPhotoUrl');
    expect(await ordersLineVariantUpdatePage.getThumbnailPhotoUrlInput()).to.match(/thumbnailPhotoUrl/);
    await ordersLineVariantUpdatePage.setPriceInput('5');
    expect(await ordersLineVariantUpdatePage.getPriceInput()).to.eq('5');
    await ordersLineVariantUpdatePage.ordersLineSelectLastOption();
    await waitUntilDisplayed(ordersLineVariantUpdatePage.getSaveButton());
    await ordersLineVariantUpdatePage.save();
    await waitUntilHidden(ordersLineVariantUpdatePage.getSaveButton());
    expect(await ordersLineVariantUpdatePage.getSaveButton().isPresent()).to.be.false;

    await ordersLineVariantComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await ordersLineVariantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last OrdersLineVariant', async () => {
    await ordersLineVariantComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await ordersLineVariantComponentsPage.countDeleteButtons();
    await ordersLineVariantComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    ordersLineVariantDeleteDialog = new OrdersLineVariantDeleteDialog();
    expect(await ordersLineVariantDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /luulposApp.ordersLineVariant.delete.question/
    );
    await ordersLineVariantDeleteDialog.clickOnConfirmButton();

    await ordersLineVariantComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await ordersLineVariantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
