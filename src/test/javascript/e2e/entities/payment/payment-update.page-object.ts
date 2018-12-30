import { element, by, ElementFinder } from 'protractor';

export default class PaymentUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.payment.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  paymentDateInput: ElementFinder = element(by.css('input#payment-paymentDate'));
  paymentProviderInput: ElementFinder = element(by.css('input#payment-paymentProvider'));
  amountInput: ElementFinder = element(by.css('input#payment-amount'));
  paymentStatusSelect: ElementFinder = element(by.css('select#payment-paymentStatus'));
  curencyInput: ElementFinder = element(by.css('input#payment-curency'));
  customerNameInput: ElementFinder = element(by.css('input#payment-customerName'));
  shopSelect: ElementFinder = element(by.css('select#payment-shop'));
  processedBySelect: ElementFinder = element(by.css('select#payment-processedBy'));
  paymentMethodSelect: ElementFinder = element(by.css('select#payment-paymentMethod'));
  orderSelect: ElementFinder = element(by.css('select#payment-order'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setPaymentDateInput(paymentDate) {
    await this.paymentDateInput.sendKeys(paymentDate);
  }

  async getPaymentDateInput() {
    return this.paymentDateInput.getAttribute('value');
  }

  async setPaymentProviderInput(paymentProvider) {
    await this.paymentProviderInput.sendKeys(paymentProvider);
  }

  async getPaymentProviderInput() {
    return this.paymentProviderInput.getAttribute('value');
  }

  async setAmountInput(amount) {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput() {
    return this.amountInput.getAttribute('value');
  }

  async setPaymentStatusSelect(paymentStatus) {
    await this.paymentStatusSelect.sendKeys(paymentStatus);
  }

  async getPaymentStatusSelect() {
    return this.paymentStatusSelect.element(by.css('option:checked')).getText();
  }

  async paymentStatusSelectLastOption() {
    await this.paymentStatusSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setCurencyInput(curency) {
    await this.curencyInput.sendKeys(curency);
  }

  async getCurencyInput() {
    return this.curencyInput.getAttribute('value');
  }

  async setCustomerNameInput(customerName) {
    await this.customerNameInput.sendKeys(customerName);
  }

  async getCustomerNameInput() {
    return this.customerNameInput.getAttribute('value');
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

  async processedBySelectLastOption() {
    await this.processedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async processedBySelectOption(option) {
    await this.processedBySelect.sendKeys(option);
  }

  getProcessedBySelect() {
    return this.processedBySelect;
  }

  async getProcessedBySelectedOption() {
    return this.processedBySelect.element(by.css('option:checked')).getText();
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

  async orderSelectLastOption() {
    await this.orderSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async orderSelectOption(option) {
    await this.orderSelect.sendKeys(option);
  }

  getOrderSelect() {
    return this.orderSelect;
  }

  async getOrderSelectedOption() {
    return this.orderSelect.element(by.css('option:checked')).getText();
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
