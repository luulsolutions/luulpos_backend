/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SectionTableComponentsPage from './section-table.page-object';
import { SectionTableDeleteDialog } from './section-table.page-object';
import SectionTableUpdatePage from './section-table-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('SectionTable e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sectionTableUpdatePage: SectionTableUpdatePage;
  let sectionTableComponentsPage: SectionTableComponentsPage;
  let sectionTableDeleteDialog: SectionTableDeleteDialog;

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

  it('should load SectionTables', async () => {
    await navBarPage.getEntityPage('section-table');
    sectionTableComponentsPage = new SectionTableComponentsPage();
    expect(await sectionTableComponentsPage.getTitle().getText()).to.match(/Section Tables/);
  });

  it('should load create SectionTable page', async () => {
    await sectionTableComponentsPage.clickOnCreateButton();
    sectionTableUpdatePage = new SectionTableUpdatePage();
    expect(await sectionTableUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.sectionTable.home.createOrEditLabel/);
  });

  it('should create and save SectionTables', async () => {
    const nbButtonsBeforeCreate = await sectionTableComponentsPage.countDeleteButtons();

    await sectionTableUpdatePage.setTableNumberInput('5');
    expect(await sectionTableUpdatePage.getTableNumberInput()).to.eq('5');
    await sectionTableUpdatePage.setDescriptionInput('description');
    expect(await sectionTableUpdatePage.getDescriptionInput()).to.match(/description/);
    await sectionTableUpdatePage.shopSectionsSelectLastOption();
    await waitUntilDisplayed(sectionTableUpdatePage.getSaveButton());
    await sectionTableUpdatePage.save();
    await waitUntilHidden(sectionTableUpdatePage.getSaveButton());
    expect(await sectionTableUpdatePage.getSaveButton().isPresent()).to.be.false;

    await sectionTableComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await sectionTableComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last SectionTable', async () => {
    await sectionTableComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await sectionTableComponentsPage.countDeleteButtons();
    await sectionTableComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    sectionTableDeleteDialog = new SectionTableDeleteDialog();
    expect(await sectionTableDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.sectionTable.delete.question/);
    await sectionTableDeleteDialog.clickOnConfirmButton();

    await sectionTableComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await sectionTableComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
