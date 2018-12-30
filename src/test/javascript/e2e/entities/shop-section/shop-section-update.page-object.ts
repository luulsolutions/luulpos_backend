import { element, by, ElementFinder } from 'protractor';

export default class ShopSectionUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.shopSection.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  sectionNameInput: ElementFinder = element(by.css('input#shop-section-sectionName'));
  descriptionInput: ElementFinder = element(by.css('input#shop-section-description'));
  surchargePercentageInput: ElementFinder = element(by.css('input#shop-section-surchargePercentage'));
  surchargeFlatAmountInput: ElementFinder = element(by.css('input#shop-section-surchargeFlatAmount'));
  usePercentageInput: ElementFinder = element(by.css('input#shop-section-usePercentage'));
  shopSelect: ElementFinder = element(by.css('select#shop-section-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setSectionNameInput(sectionName) {
    await this.sectionNameInput.sendKeys(sectionName);
  }

  async getSectionNameInput() {
    return this.sectionNameInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setSurchargePercentageInput(surchargePercentage) {
    await this.surchargePercentageInput.sendKeys(surchargePercentage);
  }

  async getSurchargePercentageInput() {
    return this.surchargePercentageInput.getAttribute('value');
  }

  async setSurchargeFlatAmountInput(surchargeFlatAmount) {
    await this.surchargeFlatAmountInput.sendKeys(surchargeFlatAmount);
  }

  async getSurchargeFlatAmountInput() {
    return this.surchargeFlatAmountInput.getAttribute('value');
  }

  getUsePercentageInput() {
    return this.usePercentageInput;
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
