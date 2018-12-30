import { element, by, ElementFinder } from 'protractor';

export default class ShopUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.shop.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  shopNameInput: ElementFinder = element(by.css('input#shop-shopName'));
  shopAccountTypeSelect: ElementFinder = element(by.css('select#shop-shopAccountType'));
  approvalDateInput: ElementFinder = element(by.css('input#shop-approvalDate'));
  addressInput: ElementFinder = element(by.css('input#shop-address'));
  emailInput: ElementFinder = element(by.css('input#shop-email'));
  descriptionInput: ElementFinder = element(by.css('input#shop-description'));
  noteInput: ElementFinder = element(by.css('input#shop-note'));
  landlandInput: ElementFinder = element(by.css('input#shop-landland'));
  mobileInput: ElementFinder = element(by.css('input#shop-mobile'));
  createdDateInput: ElementFinder = element(by.css('input#shop-createdDate'));
  shopLogoInput: ElementFinder = element(by.css('input#file_shopLogo'));
  shopLogoUrlInput: ElementFinder = element(by.css('input#shop-shopLogoUrl'));
  activeInput: ElementFinder = element(by.css('input#shop-active'));
  currencyInput: ElementFinder = element(by.css('input#shop-currency'));
  companySelect: ElementFinder = element(by.css('select#shop-company'));
  approvedBySelect: ElementFinder = element(by.css('select#shop-approvedBy'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setShopNameInput(shopName) {
    await this.shopNameInput.sendKeys(shopName);
  }

  async getShopNameInput() {
    return this.shopNameInput.getAttribute('value');
  }

  async setShopAccountTypeSelect(shopAccountType) {
    await this.shopAccountTypeSelect.sendKeys(shopAccountType);
  }

  async getShopAccountTypeSelect() {
    return this.shopAccountTypeSelect.element(by.css('option:checked')).getText();
  }

  async shopAccountTypeSelectLastOption() {
    await this.shopAccountTypeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setApprovalDateInput(approvalDate) {
    await this.approvalDateInput.sendKeys(approvalDate);
  }

  async getApprovalDateInput() {
    return this.approvalDateInput.getAttribute('value');
  }

  async setAddressInput(address) {
    await this.addressInput.sendKeys(address);
  }

  async getAddressInput() {
    return this.addressInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return this.emailInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setNoteInput(note) {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput() {
    return this.noteInput.getAttribute('value');
  }

  async setLandlandInput(landland) {
    await this.landlandInput.sendKeys(landland);
  }

  async getLandlandInput() {
    return this.landlandInput.getAttribute('value');
  }

  async setMobileInput(mobile) {
    await this.mobileInput.sendKeys(mobile);
  }

  async getMobileInput() {
    return this.mobileInput.getAttribute('value');
  }

  async setCreatedDateInput(createdDate) {
    await this.createdDateInput.sendKeys(createdDate);
  }

  async getCreatedDateInput() {
    return this.createdDateInput.getAttribute('value');
  }

  async setShopLogoInput(shopLogo) {
    await this.shopLogoInput.sendKeys(shopLogo);
  }

  async getShopLogoInput() {
    return this.shopLogoInput.getAttribute('value');
  }

  async setShopLogoUrlInput(shopLogoUrl) {
    await this.shopLogoUrlInput.sendKeys(shopLogoUrl);
  }

  async getShopLogoUrlInput() {
    return this.shopLogoUrlInput.getAttribute('value');
  }

  getActiveInput() {
    return this.activeInput;
  }
  async setCurrencyInput(currency) {
    await this.currencyInput.sendKeys(currency);
  }

  async getCurrencyInput() {
    return this.currencyInput.getAttribute('value');
  }

  async companySelectLastOption() {
    await this.companySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async companySelectOption(option) {
    await this.companySelect.sendKeys(option);
  }

  getCompanySelect() {
    return this.companySelect;
  }

  async getCompanySelectedOption() {
    return this.companySelect.element(by.css('option:checked')).getText();
  }

  async approvedBySelectLastOption() {
    await this.approvedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async approvedBySelectOption(option) {
    await this.approvedBySelect.sendKeys(option);
  }

  getApprovedBySelect() {
    return this.approvedBySelect;
  }

  async getApprovedBySelectedOption() {
    return this.approvedBySelect.element(by.css('option:checked')).getText();
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
