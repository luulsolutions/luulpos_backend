/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SystemConfigComponentsPage from './system-config.page-object';
import { SystemConfigDeleteDialog } from './system-config.page-object';
import SystemConfigUpdatePage from './system-config-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('SystemConfig e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let systemConfigUpdatePage: SystemConfigUpdatePage;
  let systemConfigComponentsPage: SystemConfigComponentsPage;
  let systemConfigDeleteDialog: SystemConfigDeleteDialog;

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

  it('should load SystemConfigs', async () => {
    await navBarPage.getEntityPage('system-config');
    systemConfigComponentsPage = new SystemConfigComponentsPage();
    expect(await systemConfigComponentsPage.getTitle().getText()).to.match(/System Configs/);
  });

  it('should load create SystemConfig page', async () => {
    await systemConfigComponentsPage.clickOnCreateButton();
    systemConfigUpdatePage = new SystemConfigUpdatePage();
    expect(await systemConfigUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.systemConfig.home.createOrEditLabel/);
  });

  it('should create and save SystemConfigs', async () => {
    const nbButtonsBeforeCreate = await systemConfigComponentsPage.countDeleteButtons();

    await systemConfigUpdatePage.setKeyInput('key');
    expect(await systemConfigUpdatePage.getKeyInput()).to.match(/key/);
    await systemConfigUpdatePage.setValueInput('value');
    expect(await systemConfigUpdatePage.getValueInput()).to.match(/value/);
    await systemConfigUpdatePage.configurationTypeSelectLastOption();
    await systemConfigUpdatePage.setNoteInput('note');
    expect(await systemConfigUpdatePage.getNoteInput()).to.match(/note/);
    const selectedEnabled = await systemConfigUpdatePage.getEnabledInput().isSelected();
    if (selectedEnabled) {
      await systemConfigUpdatePage.getEnabledInput().click();
      expect(await systemConfigUpdatePage.getEnabledInput().isSelected()).to.be.false;
    } else {
      await systemConfigUpdatePage.getEnabledInput().click();
      expect(await systemConfigUpdatePage.getEnabledInput().isSelected()).to.be.true;
    }
    await systemConfigUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(systemConfigUpdatePage.getSaveButton());
    await systemConfigUpdatePage.save();
    await waitUntilHidden(systemConfigUpdatePage.getSaveButton());
    expect(await systemConfigUpdatePage.getSaveButton().isPresent()).to.be.false;

    await systemConfigComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await systemConfigComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last SystemConfig', async () => {
    await systemConfigComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await systemConfigComponentsPage.countDeleteButtons();
    await systemConfigComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    systemConfigDeleteDialog = new SystemConfigDeleteDialog();
    expect(await systemConfigDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.systemConfig.delete.question/);
    await systemConfigDeleteDialog.clickOnConfirmButton();

    await systemConfigComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await systemConfigComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
