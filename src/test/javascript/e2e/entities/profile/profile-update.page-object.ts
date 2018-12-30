import { element, by, ElementFinder } from 'protractor';

export default class ProfileUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.profile.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  firstNameInput: ElementFinder = element(by.css('input#profile-firstName'));
  lastNameInput: ElementFinder = element(by.css('input#profile-lastName'));
  emailInput: ElementFinder = element(by.css('input#profile-email'));
  userTypeSelect: ElementFinder = element(by.css('select#profile-userType'));
  dateOfBirthInput: ElementFinder = element(by.css('input#profile-dateOfBirth'));
  genderSelect: ElementFinder = element(by.css('select#profile-gender'));
  registerationDateInput: ElementFinder = element(by.css('input#profile-registerationDate'));
  lastAccessInput: ElementFinder = element(by.css('input#profile-lastAccess'));
  profileStatusSelect: ElementFinder = element(by.css('select#profile-profileStatus'));
  telephoneInput: ElementFinder = element(by.css('input#profile-telephone'));
  mobileInput: ElementFinder = element(by.css('input#profile-mobile'));
  hourlyPayRateInput: ElementFinder = element(by.css('input#profile-hourlyPayRate'));
  thumbnailPhotoInput: ElementFinder = element(by.css('input#file_thumbnailPhoto'));
  thumbnailPhotoUrlInput: ElementFinder = element(by.css('input#profile-thumbnailPhotoUrl'));
  fullPhotoInput: ElementFinder = element(by.css('input#file_fullPhoto'));
  fullPhotoUrlInput: ElementFinder = element(by.css('input#profile-fullPhotoUrl'));
  activeInput: ElementFinder = element(by.css('input#profile-active'));
  shopChangeIdInput: ElementFinder = element(by.css('input#profile-shopChangeId'));
  userSelect: ElementFinder = element(by.css('select#profile-user'));
  shopSelect: ElementFinder = element(by.css('select#profile-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setFirstNameInput(firstName) {
    await this.firstNameInput.sendKeys(firstName);
  }

  async getFirstNameInput() {
    return this.firstNameInput.getAttribute('value');
  }

  async setLastNameInput(lastName) {
    await this.lastNameInput.sendKeys(lastName);
  }

  async getLastNameInput() {
    return this.lastNameInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return this.emailInput.getAttribute('value');
  }

  async setUserTypeSelect(userType) {
    await this.userTypeSelect.sendKeys(userType);
  }

  async getUserTypeSelect() {
    return this.userTypeSelect.element(by.css('option:checked')).getText();
  }

  async userTypeSelectLastOption() {
    await this.userTypeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setDateOfBirthInput(dateOfBirth) {
    await this.dateOfBirthInput.sendKeys(dateOfBirth);
  }

  async getDateOfBirthInput() {
    return this.dateOfBirthInput.getAttribute('value');
  }

  async setGenderSelect(gender) {
    await this.genderSelect.sendKeys(gender);
  }

  async getGenderSelect() {
    return this.genderSelect.element(by.css('option:checked')).getText();
  }

  async genderSelectLastOption() {
    await this.genderSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setRegisterationDateInput(registerationDate) {
    await this.registerationDateInput.sendKeys(registerationDate);
  }

  async getRegisterationDateInput() {
    return this.registerationDateInput.getAttribute('value');
  }

  async setLastAccessInput(lastAccess) {
    await this.lastAccessInput.sendKeys(lastAccess);
  }

  async getLastAccessInput() {
    return this.lastAccessInput.getAttribute('value');
  }

  async setProfileStatusSelect(profileStatus) {
    await this.profileStatusSelect.sendKeys(profileStatus);
  }

  async getProfileStatusSelect() {
    return this.profileStatusSelect.element(by.css('option:checked')).getText();
  }

  async profileStatusSelectLastOption() {
    await this.profileStatusSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setTelephoneInput(telephone) {
    await this.telephoneInput.sendKeys(telephone);
  }

  async getTelephoneInput() {
    return this.telephoneInput.getAttribute('value');
  }

  async setMobileInput(mobile) {
    await this.mobileInput.sendKeys(mobile);
  }

  async getMobileInput() {
    return this.mobileInput.getAttribute('value');
  }

  async setHourlyPayRateInput(hourlyPayRate) {
    await this.hourlyPayRateInput.sendKeys(hourlyPayRate);
  }

  async getHourlyPayRateInput() {
    return this.hourlyPayRateInput.getAttribute('value');
  }

  async setThumbnailPhotoInput(thumbnailPhoto) {
    await this.thumbnailPhotoInput.sendKeys(thumbnailPhoto);
  }

  async getThumbnailPhotoInput() {
    return this.thumbnailPhotoInput.getAttribute('value');
  }

  async setThumbnailPhotoUrlInput(thumbnailPhotoUrl) {
    await this.thumbnailPhotoUrlInput.sendKeys(thumbnailPhotoUrl);
  }

  async getThumbnailPhotoUrlInput() {
    return this.thumbnailPhotoUrlInput.getAttribute('value');
  }

  async setFullPhotoInput(fullPhoto) {
    await this.fullPhotoInput.sendKeys(fullPhoto);
  }

  async getFullPhotoInput() {
    return this.fullPhotoInput.getAttribute('value');
  }

  async setFullPhotoUrlInput(fullPhotoUrl) {
    await this.fullPhotoUrlInput.sendKeys(fullPhotoUrl);
  }

  async getFullPhotoUrlInput() {
    return this.fullPhotoUrlInput.getAttribute('value');
  }

  getActiveInput() {
    return this.activeInput;
  }
  async setShopChangeIdInput(shopChangeId) {
    await this.shopChangeIdInput.sendKeys(shopChangeId);
  }

  async getShopChangeIdInput() {
    return this.shopChangeIdInput.getAttribute('value');
  }

  async userSelectLastOption() {
    await this.userSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userSelectOption(option) {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect() {
    return this.userSelect;
  }

  async getUserSelectedOption() {
    return this.userSelect.element(by.css('option:checked')).getText();
  }

  async shopSelectLastOption() {
    await this.shopSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async shopSelectOption(option) {
    await this.shopSelect.sendKeys(option);
  }

  getShopSelect() {
    return this.shopSelect;
  }

  async getShopSelectedOption() {
    return this.shopSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
