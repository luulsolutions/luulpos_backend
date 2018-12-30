import { element, by, ElementFinder } from 'protractor';

export default class OrdersLineExtraUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.ordersLineExtra.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  ordersLineExtraNameInput: ElementFinder = element(by.css('input#orders-line-extra-ordersLineExtraName'));
  ordersLineExtraValueInput: ElementFinder = element(by.css('input#orders-line-extra-ordersLineExtraValue'));
  ordersLineExtraPriceInput: ElementFinder = element(by.css('input#orders-line-extra-ordersLineExtraPrice'));
  ordersOptionDescriptionInput: ElementFinder = element(by.css('input#orders-line-extra-ordersOptionDescription'));
  fullPhotoInput: ElementFinder = element(by.css('input#file_fullPhoto'));
  fullPhotoUrlInput: ElementFinder = element(by.css('input#orders-line-extra-fullPhotoUrl'));
  thumbnailPhotoInput: ElementFinder = element(by.css('input#file_thumbnailPhoto'));
  thumbnailPhotoUrlInput: ElementFinder = element(by.css('input#orders-line-extra-thumbnailPhotoUrl'));
  ordersLineVariantSelect: ElementFinder = element(by.css('select#orders-line-extra-ordersLineVariant'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setOrdersLineExtraNameInput(ordersLineExtraName) {
    await this.ordersLineExtraNameInput.sendKeys(ordersLineExtraName);
  }

  async getOrdersLineExtraNameInput() {
    return this.ordersLineExtraNameInput.getAttribute('value');
  }

  async setOrdersLineExtraValueInput(ordersLineExtraValue) {
    await this.ordersLineExtraValueInput.sendKeys(ordersLineExtraValue);
  }

  async getOrdersLineExtraValueInput() {
    return this.ordersLineExtraValueInput.getAttribute('value');
  }

  async setOrdersLineExtraPriceInput(ordersLineExtraPrice) {
    await this.ordersLineExtraPriceInput.sendKeys(ordersLineExtraPrice);
  }

  async getOrdersLineExtraPriceInput() {
    return this.ordersLineExtraPriceInput.getAttribute('value');
  }

  async setOrdersOptionDescriptionInput(ordersOptionDescription) {
    await this.ordersOptionDescriptionInput.sendKeys(ordersOptionDescription);
  }

  async getOrdersOptionDescriptionInput() {
    return this.ordersOptionDescriptionInput.getAttribute('value');
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

  async ordersLineVariantSelectLastOption() {
    await this.ordersLineVariantSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ordersLineVariantSelectOption(option) {
    await this.ordersLineVariantSelect.sendKeys(option);
  }

  getOrdersLineVariantSelect() {
    return this.ordersLineVariantSelect;
  }

  async getOrdersLineVariantSelectedOption() {
    return this.ordersLineVariantSelect.element(by.css('option:checked')).getText();
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
