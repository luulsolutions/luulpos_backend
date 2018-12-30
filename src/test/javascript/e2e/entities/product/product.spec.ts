/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ProductComponentsPage from './product.page-object';
import { ProductDeleteDialog } from './product.page-object';
import ProductUpdatePage from './product-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('Product e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productUpdatePage: ProductUpdatePage;
  let productComponentsPage: ProductComponentsPage;
  let productDeleteDialog: ProductDeleteDialog;
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

  it('should load Products', async () => {
    await navBarPage.getEntityPage('product');
    productComponentsPage = new ProductComponentsPage();
    expect(await productComponentsPage.getTitle().getText()).to.match(/Products/);
  });

  it('should load create Product page', async () => {
    await productComponentsPage.clickOnCreateButton();
    productUpdatePage = new ProductUpdatePage();
    expect(await productUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.product.home.createOrEditLabel/);
  });

  it('should create and save Products', async () => {
    const nbButtonsBeforeCreate = await productComponentsPage.countDeleteButtons();

    await productUpdatePage.setProductNameInput('productName');
    expect(await productUpdatePage.getProductNameInput()).to.match(/productName/);
    await productUpdatePage.setProductDescriptionInput('productDescription');
    expect(await productUpdatePage.getProductDescriptionInput()).to.match(/productDescription/);
    await productUpdatePage.setPriceInput('5');
    expect(await productUpdatePage.getPriceInput()).to.eq('5');
    await productUpdatePage.setQuantityInput('5');
    expect(await productUpdatePage.getQuantityInput()).to.eq('5');
    await productUpdatePage.setProductImageFullInput(absolutePath);
    await productUpdatePage.setProductImageFullUrlInput('productImageFullUrl');
    expect(await productUpdatePage.getProductImageFullUrlInput()).to.match(/productImageFullUrl/);
    await productUpdatePage.setProductImageThumbInput(absolutePath);
    await productUpdatePage.setProductImageThumbUrlInput('productImageThumbUrl');
    expect(await productUpdatePage.getProductImageThumbUrlInput()).to.match(/productImageThumbUrl/);
    await productUpdatePage.setDateCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await productUpdatePage.getDateCreatedInput()).to.contain('2001-01-01T02:30');
    await productUpdatePage.setBarcodeInput('barcode');
    expect(await productUpdatePage.getBarcodeInput()).to.match(/barcode/);
    await productUpdatePage.setSerialCodeInput('serialCode');
    expect(await productUpdatePage.getSerialCodeInput()).to.match(/serialCode/);
    await productUpdatePage.setPriorityPositionInput('5');
    expect(await productUpdatePage.getPriorityPositionInput()).to.eq('5');
    const selectedActive = await productUpdatePage.getActiveInput().isSelected();
    if (selectedActive) {
      await productUpdatePage.getActiveInput().click();
      expect(await productUpdatePage.getActiveInput().isSelected()).to.be.false;
    } else {
      await productUpdatePage.getActiveInput().click();
      expect(await productUpdatePage.getActiveInput().isSelected()).to.be.true;
    }
    const selectedIsVariantProduct = await productUpdatePage.getIsVariantProductInput().isSelected();
    if (selectedIsVariantProduct) {
      await productUpdatePage.getIsVariantProductInput().click();
      expect(await productUpdatePage.getIsVariantProductInput().isSelected()).to.be.false;
    } else {
      await productUpdatePage.getIsVariantProductInput().click();
      expect(await productUpdatePage.getIsVariantProductInput().isSelected()).to.be.true;
    }
    await productUpdatePage.productTypesSelectLastOption();
    await productUpdatePage.shopSelectLastOption();
    await productUpdatePage.discountsSelectLastOption();
    await productUpdatePage.taxesSelectLastOption();
    await productUpdatePage.categorySelectLastOption();
    await waitUntilDisplayed(productUpdatePage.getSaveButton());
    await productUpdatePage.save();
    await waitUntilHidden(productUpdatePage.getSaveButton());
    expect(await productUpdatePage.getSaveButton().isPresent()).to.be.false;

    await productComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await productComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Product', async () => {
    await productComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await productComponentsPage.countDeleteButtons();
    await productComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    productDeleteDialog = new ProductDeleteDialog();
    expect(await productDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.product.delete.question/);
    await productDeleteDialog.clickOnConfirmButton();

    await productComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await productComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
