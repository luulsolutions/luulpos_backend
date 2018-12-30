/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import CompanyComponentsPage from './company.page-object';
import { CompanyDeleteDialog } from './company.page-object';
import CompanyUpdatePage from './company-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('Company e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let companyUpdatePage: CompanyUpdatePage;
  let companyComponentsPage: CompanyComponentsPage;
  let companyDeleteDialog: CompanyDeleteDialog;
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

  it('should load Companies', async () => {
    await navBarPage.getEntityPage('company');
    companyComponentsPage = new CompanyComponentsPage();
    expect(await companyComponentsPage.getTitle().getText()).to.match(/Companies/);
  });

  it('should load create Company page', async () => {
    await companyComponentsPage.clickOnCreateButton();
    companyUpdatePage = new CompanyUpdatePage();
    expect(await companyUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.company.home.createOrEditLabel/);
  });

  it('should create and save Companies', async () => {
    const nbButtonsBeforeCreate = await companyComponentsPage.countDeleteButtons();

    await companyUpdatePage.setCompanyNameInput('companyName');
    expect(await companyUpdatePage.getCompanyNameInput()).to.match(/companyName/);
    await companyUpdatePage.setDescriptionInput('description');
    expect(await companyUpdatePage.getDescriptionInput()).to.match(/description/);
    await companyUpdatePage.setNoteInput('note');
    expect(await companyUpdatePage.getNoteInput()).to.match(/note/);
    await companyUpdatePage.setCompanyLogoInput(absolutePath);
    await companyUpdatePage.setCompanyLogoUrlInput('companyLogoUrl');
    expect(await companyUpdatePage.getCompanyLogoUrlInput()).to.match(/companyLogoUrl/);
    const selectedActive = await companyUpdatePage.getActiveInput().isSelected();
    if (selectedActive) {
      await companyUpdatePage.getActiveInput().click();
      expect(await companyUpdatePage.getActiveInput().isSelected()).to.be.false;
    } else {
      await companyUpdatePage.getActiveInput().click();
      expect(await companyUpdatePage.getActiveInput().isSelected()).to.be.true;
    }
    await waitUntilDisplayed(companyUpdatePage.getSaveButton());
    await companyUpdatePage.save();
    await waitUntilHidden(companyUpdatePage.getSaveButton());
    expect(await companyUpdatePage.getSaveButton().isPresent()).to.be.false;

    await companyComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await companyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Company', async () => {
    await companyComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await companyComponentsPage.countDeleteButtons();
    await companyComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    companyDeleteDialog = new CompanyDeleteDialog();
    expect(await companyDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.company.delete.question/);
    await companyDeleteDialog.clickOnConfirmButton();

    await companyComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await companyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
