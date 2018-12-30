import { element, by, ElementFinder } from 'protractor';

export default class TaxUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.tax.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  descriptionInput: ElementFinder = element(by.css('input#tax-description'));
  percentageInput: ElementFinder = element(by.css('input#tax-percentage'));
  amountInput: ElementFinder = element(by.css('input#tax-amount'));
  activeInput: ElementFinder = element(by.css('input#tax-active'));
  shopSelect: ElementFinder = element(by.css('select#tax-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setPercentageInput(percentage) {
    await this.percentageInput.sendKeys(percentage);
  }

  async getPercentageInput() {
    return this.percentageInput.getAttribute('value');
  }

  async setAmountInput(amount) {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput() {
    return this.amountInput.getAttribute('value');
  }

  getActiveInput() {
    return this.activeInput;
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
