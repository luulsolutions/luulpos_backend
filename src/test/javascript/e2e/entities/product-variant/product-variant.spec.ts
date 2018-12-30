/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ProductVariantComponentsPage from './product-variant.page-object';
import { ProductVariantDeleteDialog } from './product-variant.page-object';
import ProductVariantUpdatePage from './product-variant-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('ProductVariant e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productVariantUpdatePage: ProductVariantUpdatePage;
  let productVariantComponentsPage: ProductVariantComponentsPage;
  let productVariantDeleteDialog: ProductVariantDeleteDialog;
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

  it('should load ProductVariants', async () => {
    await navBarPage.getEntityPage('product-variant');
    productVariantComponentsPage = new ProductVariantComponentsPage();
    expect(await productVariantComponentsPage.getTitle().getText()).to.match(/Product Variants/);
  });

  it('should load create ProductVariant page', async () => {
    await productVariantComponentsPage.clickOnCreateButton();
    productVariantUpdatePage = new ProductVariantUpdatePage();
    expect(await productVariantUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.productVariant.home.createOrEditLabel/);
  });

  it('should create and save ProductVariants', async () => {
    const nbButtonsBeforeCreate = await productVariantComponentsPage.countDeleteButtons();

    await productVariantUpdatePage.setVariantNameInput('variantName');
    expect(await productVariantUpdatePage.getVariantNameInput()).to.match(/variantName/);
    await productVariantUpdatePage.setDescriptionInput('description');
    expect(await productVariantUpdatePage.getDescriptionInput()).to.match(/description/);
    await productVariantUpdatePage.setPercentageInput('5');
    expect(await productVariantUpdatePage.getPercentageInput()).to.eq('5');
    await productVariantUpdatePage.setFullPhotoInput(absolutePath);
    await productVariantUpdatePage.setFullPhotoUrlInput('fullPhotoUrl');
    expect(await productVariantUpdatePage.getFullPhotoUrlInput()).to.match(/fullPhotoUrl/);
    await productVariantUpdatePage.setThumbnailPhotoInput(absolutePath);
    await productVariantUpdatePage.setThumbnailPhotoUrlInput('thumbnailPhotoUrl');
    expect(await productVariantUpdatePage.getThumbnailPhotoUrlInput()).to.match(/thumbnailPhotoUrl/);
    await productVariantUpdatePage.setPriceInput('5');
    expect(await productVariantUpdatePage.getPriceInput()).to.eq('5');
    await productVariantUpdatePage.productSelectLastOption();
    await waitUntilDisplayed(productVariantUpdatePage.getSaveButton());
    await productVariantUpdatePage.save();
    await waitUntilHidden(productVariantUpdatePage.getSaveButton());
    expect(await productVariantUpdatePage.getSaveButton().isPresent()).to.be.false;

    await productVariantComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await productVariantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ProductVariant', async () => {
    await productVariantComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await productVariantComponentsPage.countDeleteButtons();
    await productVariantComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    productVariantDeleteDialog = new ProductVariantDeleteDialog();
    expect(await productVariantDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.productVariant.delete.question/);
    await productVariantDeleteDialog.clickOnConfirmButton();

    await productVariantComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await productVariantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
