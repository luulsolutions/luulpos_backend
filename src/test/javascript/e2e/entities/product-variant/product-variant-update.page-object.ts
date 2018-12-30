import { element, by, ElementFinder } from 'protractor';

export default class ProductVariantUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.productVariant.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  variantNameInput: ElementFinder = element(by.css('input#product-variant-variantName'));
  descriptionInput: ElementFinder = element(by.css('input#product-variant-description'));
  percentageInput: ElementFinder = element(by.css('input#product-variant-percentage'));
  fullPhotoInput: ElementFinder = element(by.css('input#file_fullPhoto'));
  fullPhotoUrlInput: ElementFinder = element(by.css('input#product-variant-fullPhotoUrl'));
  thumbnailPhotoInput: ElementFinder = element(by.css('input#file_thumbnailPhoto'));
  thumbnailPhotoUrlInput: ElementFinder = element(by.css('input#product-variant-thumbnailPhotoUrl'));
  priceInput: ElementFinder = element(by.css('input#product-variant-price'));
  productSelect: ElementFinder = element(by.css('select#product-variant-product'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setVariantNameInput(variantName) {
    await this.variantNameInput.sendKeys(variantName);
  }

  async getVariantNameInput() {
    return this.variantNameInput.getAttribute('value');
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
