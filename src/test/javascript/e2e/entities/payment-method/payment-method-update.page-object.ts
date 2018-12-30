import { element, by, ElementFinder } from 'protractor';

export default class PaymentMethodUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.paymentMethod.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  paymentMethodInput: ElementFinder = element(by.css('input#payment-method-paymentMethod'));
  descriptionInput: ElementFinder = element(by.css('input#payment-method-description'));
  activeInput: ElementFinder = element(by.css('input#payment-method-active'));
  shopSelect: ElementFinder = element(by.css('select#payment-method-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setPaymentMethodInput(paymentMethod) {
    await this.paymentMethodInput.sendKeys(paymentMethod);
  }

  async getPaymentMethodInput() {
    return this.paymentMethodInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
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
