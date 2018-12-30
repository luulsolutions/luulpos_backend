/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ProfileComponentsPage from './profile.page-object';
import { ProfileDeleteDialog } from './profile.page-object';
import ProfileUpdatePage from './profile-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('Profile e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let profileUpdatePage: ProfileUpdatePage;
  let profileComponentsPage: ProfileComponentsPage;
  let profileDeleteDialog: ProfileDeleteDialog;
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

  it('should load Profiles', async () => {
    await navBarPage.getEntityPage('profile');
    profileComponentsPage = new ProfileComponentsPage();
    expect(await profileComponentsPage.getTitle().getText()).to.match(/Profiles/);
  });

  it('should load create Profile page', async () => {
    await profileComponentsPage.clickOnCreateButton();
    profileUpdatePage = new ProfileUpdatePage();
    expect(await profileUpdatePage.getPageTitle().getAttribute('id')).to.match(/luulposApp.profile.home.createOrEditLabel/);
  });

  it('should create and save Profiles', async () => {
    const nbButtonsBeforeCreate = await profileComponentsPage.countDeleteButtons();

    await profileUpdatePage.setFirstNameInput('firstName');
    expect(await profileUpdatePage.getFirstNameInput()).to.match(/firstName/);
    await profileUpdatePage.setLastNameInput('lastName');
    expect(await profileUpdatePage.getLastNameInput()).to.match(/lastName/);
    await profileUpdatePage.setEmailInput('email');
    expect(await profileUpdatePage.getEmailInput()).to.match(/email/);
    await profileUpdatePage.userTypeSelectLastOption();
    await profileUpdatePage.setDateOfBirthInput('01-01-2001');
    expect(await profileUpdatePage.getDateOfBirthInput()).to.eq('2001-01-01');
    await profileUpdatePage.genderSelectLastOption();
    await profileUpdatePage.setRegisterationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await profileUpdatePage.getRegisterationDateInput()).to.contain('2001-01-01T02:30');
    await profileUpdatePage.setLastAccessInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await profileUpdatePage.getLastAccessInput()).to.contain('2001-01-01T02:30');
    await profileUpdatePage.profileStatusSelectLastOption();
    await profileUpdatePage.setTelephoneInput('telephone');
    expect(await profileUpdatePage.getTelephoneInput()).to.match(/telephone/);
    await profileUpdatePage.setMobileInput('mobile');
    expect(await profileUpdatePage.getMobileInput()).to.match(/mobile/);
    await profileUpdatePage.setHourlyPayRateInput('5');
    expect(await profileUpdatePage.getHourlyPayRateInput()).to.eq('5');
    await profileUpdatePage.setThumbnailPhotoInput(absolutePath);
    await profileUpdatePage.setThumbnailPhotoUrlInput('thumbnailPhotoUrl');
    expect(await profileUpdatePage.getThumbnailPhotoUrlInput()).to.match(/thumbnailPhotoUrl/);
    await profileUpdatePage.setFullPhotoInput(absolutePath);
    await profileUpdatePage.setFullPhotoUrlInput('fullPhotoUrl');
    expect(await profileUpdatePage.getFullPhotoUrlInput()).to.match(/fullPhotoUrl/);
    const selectedActive = await profileUpdatePage.getActiveInput().isSelected();
    if (selectedActive) {
      await profileUpdatePage.getActiveInput().click();
      expect(await profileUpdatePage.getActiveInput().isSelected()).to.be.false;
    } else {
      await profileUpdatePage.getActiveInput().click();
      expect(await profileUpdatePage.getActiveInput().isSelected()).to.be.true;
    }
    await profileUpdatePage.setShopChangeIdInput('5');
    expect(await profileUpdatePage.getShopChangeIdInput()).to.eq('5');
    await profileUpdatePage.userSelectLastOption();
    await profileUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(profileUpdatePage.getSaveButton());
    await profileUpdatePage.save();
    await waitUntilHidden(profileUpdatePage.getSaveButton());
    expect(await profileUpdatePage.getSaveButton().isPresent()).to.be.false;

    await profileComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await profileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Profile', async () => {
    await profileComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await profileComponentsPage.countDeleteButtons();
    await profileComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    profileDeleteDialog = new ProfileDeleteDialog();
    expect(await profileDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/luulposApp.profile.delete.question/);
    await profileDeleteDialog.clickOnConfirmButton();

    await profileComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await profileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
