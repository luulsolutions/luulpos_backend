import { element, by, ElementFinder } from 'protractor';

export default class OrdersLineUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.ordersLine.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  ordersLineNameInput: ElementFinder = element(by.css('input#orders-line-ordersLineName'));
  ordersLineValueInput: ElementFinder = element(by.css('input#orders-line-ordersLineValue'));
  ordersLinePriceInput: ElementFinder = element(by.css('input#orders-line-ordersLinePrice'));
  ordersLineDescriptionInput: ElementFinder = element(by.css('input#orders-line-ordersLineDescription'));
  thumbnailPhotoInput: ElementFinder = element(by.css('input#file_thumbnailPhoto'));
  fullPhotoInput: ElementFinder = element(by.css('input#file_fullPhoto'));
  fullPhotoUrlInput: ElementFinder = element(by.css('input#orders-line-fullPhotoUrl'));
  thumbnailPhotoUrlInput: ElementFinder = element(by.css('input#orders-line-thumbnailPhotoUrl'));
  ordersSelect: ElementFinder = element(by.css('select#orders-line-orders'));
  productSelect: ElementFinder = element(by.css('select#orders-line-product'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setOrdersLineNameInput(ordersLineName) {
    await this.ordersLineNameInput.sendKeys(ordersLineName);
  }

  async getOrdersLineNameInput() {
    return this.ordersLineNameInput.getAttribute('value');
  }

  async setOrdersLineValueInput(ordersLineValue) {
    await this.ordersLineValueInput.sendKeys(ordersLineValue);
  }

  async getOrdersLineValueInput() {
    return this.ordersLineValueInput.getAttribute('value');
  }

  async setOrdersLinePriceInput(ordersLinePrice) {
    await this.ordersLinePriceInput.sendKeys(ordersLinePrice);
  }

  async getOrdersLinePriceInput() {
    return this.ordersLinePriceInput.getAttribute('value');
  }

  async setOrdersLineDescriptionInput(ordersLineDescription) {
    await this.ordersLineDescriptionInput.sendKeys(ordersLineDescription);
  }

  async getOrdersLineDescriptionInput() {
    return this.ordersLineDescriptionInput.getAttribute('value');
  }

  async setThumbnailPhotoInput(thumbnailPhoto) {
    await this.thumbnailPhotoInput.sendKeys(thumbnailPhoto);
  }

  async getThumbnailPhotoInput() {
    return this.thumbnailPhotoInput.getAttribute('value');
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

  async setThumbnailPhotoUrlInput(thumbnailPhotoUrl) {
    await this.thumbnailPhotoUrlInput.sendKeys(thumbnailPhotoUrl);
  }

  async getThumbnailPhotoUrlInput() {
    return this.thumbnailPhotoUrlInput.getAttribute('value');
  }

  async ordersSelectLastOption() {
    await this.ordersSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ordersSelectOption(option) {
    await this.ordersSelect.sendKeys(option);
  }

  getOrdersSelect() {
    return this.ordersSelect;
  }

  async getOrdersSelectedOption() {
    return this.ordersSelect.element(by.css('option:checked')).getText();
  }

  async productSelectLastOption() {
    await this.productSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async productSelectOption(option) {
    await this.productSelect.sendKeys(option);
  }

  getProductSelect() {
    return this.productSelect;
  }

  async getProductSelectedOption() {
    return this.productSelect.element(by.css('option:checked')).getText();
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
