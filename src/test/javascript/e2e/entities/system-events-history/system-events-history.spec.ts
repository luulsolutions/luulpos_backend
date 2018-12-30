/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SystemEventsHistoryComponentsPage from './system-events-history.page-object';
import { SystemEventsHistoryDeleteDialog } from './system-events-history.page-object';
import SystemEventsHistoryUpdatePage from './system-events-history-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('SystemEventsHistory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let systemEventsHistoryUpdatePage: SystemEventsHistoryUpdatePage;
  let systemEventsHistoryComponentsPage: SystemEventsHistoryComponentsPage;
  let systemEventsHistoryDeleteDialog: SystemEventsHistoryDeleteDialog;

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

  it('should load SystemEventsHistories', async () => {
    await navBarPage.getEntityPage('system-events-history');
    systemEventsHistoryComponentsPage = new SystemEventsHistoryComponentsPage();
    expect(await systemEventsHistoryComponentsPage.getTitle().getText()).to.match(/System Events Histories/);
  });

  it('should load create SystemEventsHistory page', async () => {
    await systemEventsHistoryComponentsPage.clickOnCreateButton();
    systemEventsHistoryUpdatePage = new SystemEventsHistoryUpdatePage();
    expect(await systemEventsHistoryUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /luulposApp.systemEventsHistory.home.createOrEditLabel/
    );
  });

  it('should create and save SystemEventsHistories', async () => {
    const nbButtonsBeforeCreate = await systemEventsHistoryComponentsPage.countDeleteButtons();

    await systemEventsHistoryUpdatePage.setEventNameInput('eventName');
    expect(await systemEventsHistoryUpdatePage.getEventNameInput()).to.match(/eventName/);
    await systemEventsHistoryUpdatePage.setEventDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await systemEventsHistoryUpdatePage.getEventDateInput()).to.contain('2001-01-01T02:30');
    await systemEventsHistoryUpdatePage.setEventApiInput('eventApi');
    expect(await systemEventsHistoryUpdatePage.getEventApiInput()).to.match(/eventApi/);
    await systemEventsHistoryUpdatePage.setEventNoteInput('eventNote');
    expect(await systemEventsHistoryUpdatePage.getEventNoteInput()).to.match(/eventNote/);
    await systemEventsHistoryUpdatePage.setEventEntityNameInput('eventEntityName');
    expect(await systemEventsHistoryUpdatePage.getEventEntityNameInput()).to.match(/eventEntityName/);
    await systemEventsHistoryUpdatePage.setEventEntityIdInput('5');
    expect(await systemEventsHistoryUpdatePage.getEventEntityIdInput()).to.eq('5');
    await systemEventsHistoryUpdatePage.triggedBySelectLastOption();
    await waitUntilDisplayed(systemEventsHistoryUpdatePage.getSaveButton());
    await systemEventsHistoryUpdatePage.save();
    await waitUntilHidden(systemEventsHistoryUpdatePage.getSaveButton());
    expect(await systemEventsHistoryUpdatePage.getSaveButton().isPresent()).to.be.false;

    await systemEventsHistoryComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await systemEventsHistoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last SystemEventsHistory', async () => {
    await systemEventsHistoryComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await systemEventsHistoryComponentsPage.countDeleteButtons();
    await systemEventsHistoryComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    systemEventsHistoryDeleteDialog = new SystemEventsHistoryDeleteDialog();
    expect(await systemEventsHistoryDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /luulposApp.systemEventsHistory.delete.question/
    );
    await systemEventsHistoryDeleteDialog.clickOnConfirmButton();

    await systemEventsHistoryComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await systemEventsHistoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
