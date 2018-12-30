import { element, by, ElementFinder } from 'protractor';

export default class OrdersLineVariantUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.ordersLineVariant.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  variantNameInput: ElementFinder = element(by.css('input#orders-line-variant-variantName'));
  variantValueInput: ElementFinder = element(by.css('input#orders-line-variant-variantValue'));
  descriptionInput: ElementFinder = element(by.css('input#orders-line-variant-description'));
  percentageInput: ElementFinder = element(by.css('input#orders-line-variant-percentage'));
  fullPhotoInput: ElementFinder = element(by.css('input#file_fullPhoto'));
  fullPhotoUrlInput: ElementFinder = element(by.css('input#orders-line-variant-fullPhotoUrl'));
  thumbnailPhotoInput: ElementFinder = element(by.css('input#file_thumbnailPhoto'));
  thumbnailPhotoUrlInput: ElementFinder = element(by.css('input#orders-line-variant-thumbnailPhotoUrl'));
  priceInput: ElementFinder = element(by.css('input#orders-line-variant-price'));
  ordersLineSelect: ElementFinder = element(by.css('select#orders-line-variant-ordersLine'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setVariantNameInput(variantName) {
    await this.variantNameInput.sendKeys(variantName);
  }

  async getVariantNameInput() {
    return this.variantNameInput.getAttribute('value');
  }

  async setVariantValueInput(variantValue) {
    await this.variantValueInput.sendKeys(variantValue);
  }

  async getVariantValueInput() {
    return this.variantValueInput.getAttribute('value');
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

  async setPriceInput(price) {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput() {
    return this.priceInput.getAttribute('value');
  }

  async ordersLineSelectLastOption() {
    await this.ordersLineSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ordersLineSelectOption(option) {
    await this.ordersLineSelect.sendKeys(option);
  }

  getOrdersLineSelect() {
    return this.ordersLineSelect;
  }

  async getOrdersLineSelectedOption() {
    return this.ordersLineSelect.element(by.css('option:checked')).getText();
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
