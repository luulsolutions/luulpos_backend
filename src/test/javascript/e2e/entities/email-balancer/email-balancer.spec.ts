/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import EmailBalancerComponentsPage from './email-balancer.page-object';
import { EmailBalancerDeleteDialog } from './email-balancer.page-object';
import EmailBalancerUpdatePage from './email-balancer-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('EmailBalancer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let emailBalancerUpdatePage: EmailBalancerUpdatePage;
  let emailBalancerComponentsPage: EmailBalancerComponentsPage;
  let emailBalancerDeleteDialog: EmailBalancerDeleteDialog;

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

  it('should load EmailBalancers', async () => {
    await navBarPage.getEntityPage('email-balancer');
    emailBalancerComponentsPage = new EmailBalancerComponentsPage();
    expect(await emailBalancerComponentsPage.getTitle().getText()).to.match(/Email Balancers/);
  });

  it('should load create EmailBalancer page', async () => {
    await emailBalancerComponentsPage.clickOnCreateButton();
    emailBalancerUpdatePage = new EmailBalancerUpdatePage();
    expect(await emailBalancerUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.emailBalancer.home.createOrEditLabel/);
  });

  it('should create and save EmailBalancers', async () => {
    const nbButtonsBeforeCreate = await emailBalancerComponentsPage.countDeleteButtons();

    await emailBalancerUpdatePage.setRelayIdInput('relayId');
    expect(await emailBalancerUpdatePage.getRelayIdInput()).to.match(/relayId/);
    await emailBalancerUpdatePage.setRelayPasswordInput('relayPassword');
    expect(await emailBalancerUpdatePage.getRelayPasswordInput()).to.match(/relayPassword/);
    await emailBalancerUpdatePage.setStartingCountInput('5');
    expect(await emailBalancerUpdatePage.getStartingCountInput()).to.eq('5');
    await emailBalancerUpdatePage.setEndingCountInput('5');
    expect(await emailBalancerUpdatePage.getEndingCountInput()).to.eq('5');
    await emailBalancerUpdatePage.setProviderInput('provider');
    expect(await emailBalancerUpdatePage.getProviderInput()).to.match(/provider/);
    await emailBalancerUpdatePage.setRelayPortInput('5');
    expect(await emailBalancerUpdatePage.getRelayPortInput()).to.eq('5');
    const selectedEnabled = await emailBalancerUpdatePage.getEnabledInput().isSelected();
    if (selectedEnabled) {
      await emailBalancerUpdatePage.getEnabledInput().click();
      expect(await emailBalancerUpdatePage.getEnabledInput().isSelected()).to.be.false;
    } else {
      await emailBalancerUpdatePage.getEnabledInput().click();
      expect(await emailBalancerUpdatePage.getEnabledInput().isSelected()).to.be.true;
    }
    await waitUntilDisplayed(emailBalancerUpdatePage.getSaveButton());
    await emailBalancerUpdatePage.save();
    await waitUntilHidden(emailBalancerUpdatePage.getSaveButton());
    expect(await emailBalancerUpdatePage.getSaveButton().isPresent()).to.be.false;

    await emailBalancerComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await emailBalancerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last EmailBalancer', async () => {
    await emailBalancerComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await emailBalancerComponentsPage.countDeleteButtons();
    await emailBalancerComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    emailBalancerDeleteDialog = new EmailBalancerDeleteDialog();
    expect(await emailBalancerDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.emailBalancer.delete.question/);
    await emailBalancerDeleteDialog.clickOnConfirmButton();

    await emailBalancerComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await emailBalancerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
