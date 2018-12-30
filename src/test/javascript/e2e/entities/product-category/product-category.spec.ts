/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ProductCategoryComponentsPage from './product-category.page-object';
import { ProductCategoryDeleteDialog } from './product-category.page-object';
import ProductCategoryUpdatePage from './product-category-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('ProductCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productCategoryUpdatePage: ProductCategoryUpdatePage;
  let productCategoryComponentsPage: ProductCategoryComponentsPage;
  let productCategoryDeleteDialog: ProductCategoryDeleteDialog;
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

  it('should load ProductCategories', async () => {
    await navBarPage.getEntityPage('product-category');
    productCategoryComponentsPage = new ProductCategoryComponentsPage();
    expect(await productCategoryComponentsPage.getTitle().getText()).to.match(/Product Categories/);
  });

  it('should load create ProductCategory page', async () => {
    await productCategoryComponentsPage.clickOnCreateButton();
    productCategoryUpdatePage = new ProductCategoryUpdatePage();
    expect(await productCategoryUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.productCategory.home.createOrEditLabel/);
  });

  it('should create and save ProductCategories', async () => {
    const nbButtonsBeforeCreate = await productCategoryComponentsPage.countDeleteButtons();

    await productCategoryUpdatePage.setCategoryInput('category');
    expect(await productCategoryUpdatePage.getCategoryInput()).to.match(/category/);
    await productCategoryUpdatePage.setDescriptionInput('description');
    expect(await productCategoryUpdatePage.getDescriptionInput()).to.match(/description/);
    await productCategoryUpdatePage.setImageFullInput(absolutePath);
    await productCategoryUpdatePage.setImageFullUrlInput('imageFullUrl');
    expect(await productCategoryUpdatePage.getImageFullUrlInput()).to.match(/imageFullUrl/);
    await productCategoryUpdatePage.setImageThumbInput(absolutePath);
    await productCategoryUpdatePage.setImageThumbUrlInput('imageThumbUrl');
    expect(await productCategoryUpdatePage.getImageThumbUrlInput()).to.match(/imageThumbUrl/);
    await productCategoryUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(productCategoryUpdatePage.getSaveButton());
    await productCategoryUpdatePage.save();
    await waitUntilHidden(productCategoryUpdatePage.getSaveButton());
    expect(await productCategoryUpdatePage.getSaveButton().isPresent()).to.be.false;

    await productCategoryComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await productCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ProductCategory', async () => {
    await productCategoryComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await productCategoryComponentsPage.countDeleteButtons();
    await productCategoryComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    productCategoryDeleteDialog = new ProductCategoryDeleteDialog();
    expect(await productCategoryDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.productCategory.delete.question/);
    await productCategoryDeleteDialog.clickOnConfirmButton();

    await productCategoryComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await productCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
