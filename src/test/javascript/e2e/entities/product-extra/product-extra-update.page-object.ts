import { element, by, ElementFinder } from 'protractor';

export default class ProductExtraUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.productExtra.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  extraNameInput: ElementFinder = element(by.css('input#product-extra-extraName'));
  descriptionInput: ElementFinder = element(by.css('input#product-extra-description'));
  extraValueInput: ElementFinder = element(by.css('input#product-extra-extraValue'));
  fullPhotoInput: ElementFinder = element(by.css('input#file_fullPhoto'));
  fullPhotoUrlInput: ElementFinder = element(by.css('input#product-extra-fullPhotoUrl'));
  thumbnailPhotoInput: ElementFinder = element(by.css('input#file_thumbnailPhoto'));
  thumbnailPhotoUrlInput: ElementFinder = element(by.css('input#product-extra-thumbnailPhotoUrl'));
  productSelect: ElementFinder = element(by.css('select#product-extra-product'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setExtraNameInput(extraName) {
    await this.extraNameInput.sendKeys(extraName);
  }

  async getExtraNameInput() {
    return this.extraNameInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setExtraValueInput(extraValue) {
    await this.extraValueInput.sendKeys(extraValue);
  }

  async getExtraValueInput() {
    return this.extraValueInput.getAttribute('value');
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
