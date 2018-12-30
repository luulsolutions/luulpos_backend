import { element, by, ElementFinder } from 'protractor';

export default class OrdersUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.orders.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  descriptionInput: ElementFinder = element(by.css('input#orders-description'));
  customerNameInput: ElementFinder = element(by.css('input#orders-customerName'));
  totalPriceInput: ElementFinder = element(by.css('input#orders-totalPrice'));
  quantityInput: ElementFinder = element(by.css('input#orders-quantity'));
  discountPercentageInput: ElementFinder = element(by.css('input#orders-discountPercentage'));
  discountAmountInput: ElementFinder = element(by.css('input#orders-discountAmount'));
  taxPercentageInput: ElementFinder = element(by.css('input#orders-taxPercentage'));
  taxAmountInput: ElementFinder = element(by.css('input#orders-taxAmount'));
  orderStatusSelect: ElementFinder = element(by.css('select#orders-orderStatus'));
  noteInput: ElementFinder = element(by.css('input#orders-note'));
  orderDateInput: ElementFinder = element(by.css('input#orders-orderDate'));
  isVariantOrderInput: ElementFinder = element(by.css('input#orders-isVariantOrder'));
  paymentMethodSelect: ElementFinder = element(by.css('select#orders-paymentMethod'));
  soldBySelect: ElementFinder = element(by.css('select#orders-soldBy'));
  preparedBySelect: ElementFinder = element(by.css('select#orders-preparedBy'));
  shopDeviceSelect: ElementFinder = element(by.css('select#orders-shopDevice'));
  sectionTableSelect: ElementFinder = element(by.css('select#orders-sectionTable'));
  shopSelect: ElementFinder = element(by.css('select#orders-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setCustomerNameInput(customerName) {
    await this.customerNameInput.sendKeys(customerName);
  }

  async getCustomerNameInput() {
    return this.customerNameInput.getAttribute('value');
  }

  async setTotalPriceInput(totalPrice) {
    await this.totalPriceInput.sendKeys(totalPrice);
  }

  async getTotalPriceInput() {
    return this.totalPriceInput.getAttribute('value');
  }

  async setQuantityInput(quantity) {
    await this.quantityInput.sendKeys(quantity);
  }

  async getQuantityInput() {
    return this.quantityInput.getAttribute('value');
  }

  async setDiscountPercentageInput(discountPercentage) {
    await this.discountPercentageInput.sendKeys(discountPercentage);
  }

  async getDiscountPercentageInput() {
    return this.discountPercentageInput.getAttribute('value');
  }

  async setDiscountAmountInput(discountAmount) {
    await this.discountAmountInput.sendKeys(discountAmount);
  }

  async getDiscountAmountInput() {
    return this.discountAmountInput.getAttribute('value');
  }

  async setTaxPercentageInput(taxPercentage) {
    await this.taxPercentageInput.sendKeys(taxPercentage);
  }

  async getTaxPercentageInput() {
    return this.taxPercentageInput.getAttribute('value');
  }

  async setTaxAmountInput(taxAmount) {
    await this.taxAmountInput.sendKeys(taxAmount);
  }

  async getTaxAmountInput() {
    return this.taxAmountInput.getAttribute('value');
  }

  async setOrderStatusSelect(orderStatus) {
    await this.orderStatusSelect.sendKeys(orderStatus);
  }

  async getOrderStatusSelect() {
    return this.orderStatusSelect.element(by.css('option:checked')).getText();
  }

  async orderStatusSelectLastOption() {
    await this.orderStatusSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setNoteInput(note) {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput() {
    return this.noteInput.getAttribute('value');
  }

  async setOrderDateInput(orderDate) {
    await this.orderDateInput.sendKeys(orderDate);
  }

  async getOrderDateInput() {
    return this.orderDateInput.getAttribute('value');
  }

  getIsVariantOrderInput() {
    return this.isVariantOrderInput;
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

  async soldBySelectLastOption() {
    await this.soldBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async soldBySelectOption(option) {
    await this.soldBySelect.sendKeys(option);
  }

  getSoldBySelect() {
    return this.soldBySelect;
  }

  async getSoldBySelectedOption() {
    return this.soldBySelect.element(by.css('option:checked')).getText();
  }

  async preparedBySelectLastOption() {
    await this.preparedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async preparedBySelectOption(option) {
    await this.preparedBySelect.sendKeys(option);
  }

  getPreparedBySelect() {
    return this.preparedBySelect;
  }

  async getPreparedBySelectedOption() {
    return this.preparedBySelect.element(by.css('option:checked')).getText();
  }

  async shopDeviceSelectLastOption() {
    await this.shopDeviceSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async shopDeviceSelectOption(option) {
    await this.shopDeviceSelect.sendKeys(option);
  }

  getShopDeviceSelect() {
    return this.shopDeviceSelect;
  }

  async getShopDeviceSelectedOption() {
    return this.shopDeviceSelect.element(by.css('option:checked')).getText();
  }

  async sectionTableSelectLastOption() {
    await this.sectionTableSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async sectionTableSelectOption(option) {
    await this.sectionTableSelect.sendKeys(option);
  }

  getSectionTableSelect() {
    return this.sectionTableSelect;
  }

  async getSectionTableSelectedOption() {
    return this.sectionTableSelect.element(by.css('option:checked')).getText();
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
