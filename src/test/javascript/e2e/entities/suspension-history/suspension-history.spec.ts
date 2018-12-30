/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SuspensionHistoryComponentsPage from './suspension-history.page-object';
import { SuspensionHistoryDeleteDialog } from './suspension-history.page-object';
import SuspensionHistoryUpdatePage from './suspension-history-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('SuspensionHistory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let suspensionHistoryUpdatePage: SuspensionHistoryUpdatePage;
  let suspensionHistoryComponentsPage: SuspensionHistoryComponentsPage;
  let suspensionHistoryDeleteDialog: SuspensionHistoryDeleteDialog;

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

  it('should load SuspensionHistories', async () => {
    await navBarPage.getEntityPage('suspension-history');
    suspensionHistoryComponentsPage = new SuspensionHistoryComponentsPage();
    expect(await suspensionHistoryComponentsPage.getTitle().getText()).to.match(/Suspension Histories/);
  });

  it('should load create SuspensionHistory page', async () => {
    await suspensionHistoryComponentsPage.clickOnCreateButton();
    suspensionHistoryUpdatePage = new SuspensionHistoryUpdatePage();
    expect(await suspensionHistoryUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /luulposApp.suspensionHistory.home.createOrEditLabel/
    );
  });

  it('should create and save SuspensionHistories', async () => {
    const nbButtonsBeforeCreate = await suspensionHistoryComponentsPage.countDeleteButtons();

    await suspensionHistoryUpdatePage.setSuspendedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await suspensionHistoryUpdatePage.getSuspendedDateInput()).to.contain('2001-01-01T02:30');
    await suspensionHistoryUpdatePage.suspensionTypeSelectLastOption();
    await suspensionHistoryUpdatePage.setSuspendedReasonInput('suspendedReason');
    expect(await suspensionHistoryUpdatePage.getSuspendedReasonInput()).to.match(/suspendedReason/);
    await suspensionHistoryUpdatePage.setResolutionNoteInput('resolutionNote');
    expect(await suspensionHistoryUpdatePage.getResolutionNoteInput()).to.match(/resolutionNote/);
    await suspensionHistoryUpdatePage.setUnsuspensionDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await suspensionHistoryUpdatePage.getUnsuspensionDateInput()).to.contain('2001-01-01T02:30');
    await suspensionHistoryUpdatePage.profileSelectLastOption();
    await suspensionHistoryUpdatePage.suspendedBySelectLastOption();
    await waitUntilDisplayed(suspensionHistoryUpdatePage.getSaveButton());
    await suspensionHistoryUpdatePage.save();
    await waitUntilHidden(suspensionHistoryUpdatePage.getSaveButton());
    expect(await suspensionHistoryUpdatePage.getSaveButton().isPresent()).to.be.false;

    await suspensionHistoryComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await suspensionHistoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last SuspensionHistory', async () => {
    await suspensionHistoryComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await suspensionHistoryComponentsPage.countDeleteButtons();
    await suspensionHistoryComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    suspensionHistoryDeleteDialog = new SuspensionHistoryDeleteDialog();
    expect(await suspensionHistoryDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /luulposApp.suspensionHistory.delete.question/
    );
    await suspensionHistoryDeleteDialog.clickOnConfirmButton();

    await suspensionHistoryComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await suspensionHistoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
