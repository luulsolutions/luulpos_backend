/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ProductTypeComponentsPage from './product-type.page-object';
import { ProductTypeDeleteDialog } from './product-type.page-object';
import ProductTypeUpdatePage from './product-type-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('ProductType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productTypeUpdatePage: ProductTypeUpdatePage;
  let productTypeComponentsPage: ProductTypeComponentsPage;
  let productTypeDeleteDialog: ProductTypeDeleteDialog;

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

  it('should load ProductTypes', async () => {
    await navBarPage.getEntityPage('product-type');
    productTypeComponentsPage = new ProductTypeComponentsPage();
    expect(await productTypeComponentsPage.getTitle().getText()).to.match(/Product Types/);
  });

  it('should load create ProductType page', async () => {
    await productTypeComponentsPage.clickOnCreateButton();
    productTypeUpdatePage = new ProductTypeUpdatePage();
    expect(await productTypeUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.productType.home.createOrEditLabel/);
  });

  it('should create and save ProductTypes', async () => {
    const nbButtonsBeforeCreate = await productTypeComponentsPage.countDeleteButtons();

    await productTypeUpdatePage.setProductTypeInput('productType');
    expect(await productTypeUpdatePage.getProductTypeInput()).to.match(/productType/);
    await productTypeUpdatePage.setProductTypeDescriptionInput('productTypeDescription');
    expect(await productTypeUpdatePage.getProductTypeDescriptionInput()).to.match(/productTypeDescription/);
    await productTypeUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(productTypeUpdatePage.getSaveButton());
    await productTypeUpdatePage.save();
    await waitUntilHidden(productTypeUpdatePage.getSaveButton());
    expect(await productTypeUpdatePage.getSaveButton().isPresent()).to.be.false;

    await productTypeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await productTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ProductType', async () => {
    await productTypeComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await productTypeComponentsPage.countDeleteButtons();
    await productTypeComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    productTypeDeleteDialog = new ProductTypeDeleteDialog();
    expect(await productTypeDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.productType.delete.question/);
    await productTypeDeleteDialog.clickOnConfirmButton();

    await productTypeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await productTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
