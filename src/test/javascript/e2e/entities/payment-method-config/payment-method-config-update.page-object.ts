import { element, by, ElementFinder } from 'protractor';

export default class PaymentMethodConfigUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.paymentMethodConfig.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  keyInput: ElementFinder = element(by.css('input#payment-method-config-key'));
  valueInput: ElementFinder = element(by.css('input#payment-method-config-value'));
  noteInput: ElementFinder = element(by.css('input#payment-method-config-note'));
  enabledInput: ElementFinder = element(by.css('input#payment-method-config-enabled'));
  paymentMethodSelect: ElementFinder = element(by.css('select#payment-method-config-paymentMethod'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setKeyInput(key) {
    await this.keyInput.sendKeys(key);
  }

  async getKeyInput() {
    return this.keyInput.getAttribute('value');
  }

  async setValueInput(value) {
    await this.valueInput.sendKeys(value);
  }

  async getValueInput() {
    return this.valueInput.getAttribute('value');
  }

  async setNoteInput(note) {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput() {
    return this.noteInput.getAttribute('value');
  }

  getEnabledInput() {
    return this.enabledInput;
  }
  async paymentMethodSelectLastOption() {
    await this.paymentMethodSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async paymentMethodSelectOption(option) {
    await this.paymentMethodSelect.sendKeys(option);
  }

  getPaymentMethodSelect() {
    return this.paymentMethodSelect;
  }

  async getPaymentMethodSelectedOption() {
    return this.paymentMethodSelect.element(by.css('option:checked')).getText();
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
