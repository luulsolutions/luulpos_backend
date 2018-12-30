/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ProductExtraComponentsPage from './product-extra.page-object';
import { ProductExtraDeleteDialog } from './product-extra.page-object';
import ProductExtraUpdatePage from './product-extra-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('ProductExtra e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productExtraUpdatePage: ProductExtraUpdatePage;
  let productExtraComponentsPage: ProductExtraComponentsPage;
  let productExtraDeleteDialog: ProductExtraDeleteDialog;
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

  it('should load ProductExtras', async () => {
    await navBarPage.getEntityPage('product-extra');
    productExtraComponentsPage = new ProductExtraComponentsPage();
    expect(await productExtraComponentsPage.getTitle().getText()).to.match(/Product Extras/);
  });

  it('should load create ProductExtra page', async () => {
    await productExtraComponentsPage.clickOnCreateButton();
    productExtraUpdatePage = new ProductExtraUpdatePage();
    expect(await productExtraUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.productExtra.home.createOrEditLabel/);
  });

  it('should create and save ProductExtras', async () => {
    const nbButtonsBeforeCreate = await productExtraComponentsPage.countDeleteButtons();

    await productExtraUpdatePage.setExtraNameInput('extraName');
    expect(await productExtraUpdatePage.getExtraNameInput()).to.match(/extraName/);
    await productExtraUpdatePage.setDescriptionInput('description');
    expect(await productExtraUpdatePage.getDescriptionInput()).to.match(/description/);
    await productExtraUpdatePage.setExtraValueInput('5');
    expect(await productExtraUpdatePage.getExtraValueInput()).to.eq('5');
    await productExtraUpdatePage.setFullPhotoInput(absolutePath);
    await productExtraUpdatePage.setFullPhotoUrlInput('fullPhotoUrl');
    expect(await productExtraUpdatePage.getFullPhotoUrlInput()).to.match(/fullPhotoUrl/);
    await productExtraUpdatePage.setThumbnailPhotoInput(absolutePath);
    await productExtraUpdatePage.setThumbnailPhotoUrlInput('thumbnailPhotoUrl');
    expect(await productExtraUpdatePage.getThumbnailPhotoUrlInput()).to.match(/thumbnailPhotoUrl/);
    await productExtraUpdatePage.productSelectLastOption();
    await waitUntilDisplayed(productExtraUpdatePage.getSaveButton());
    await productExtraUpdatePage.save();
    await waitUntilHidden(productExtraUpdatePage.getSaveButton());
    expect(await productExtraUpdatePage.getSaveButton().isPresent()).to.be.false;

    await productExtraComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await productExtraComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ProductExtra', async () => {
    await productExtraComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await productExtraComponentsPage.countDeleteButtons();
    await productExtraComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    productExtraDeleteDialog = new ProductExtraDeleteDialog();
    expect(await productExtraDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.productExtra.delete.question/);
    await productExtraDeleteDialog.clickOnConfirmButton();

    await productExtraComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await productExtraComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
