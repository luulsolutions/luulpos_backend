import { element, by, ElementFinder } from 'protractor';

export default class ProductCategoryUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.productCategory.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  categoryInput: ElementFinder = element(by.css('input#product-category-category'));
  descriptionInput: ElementFinder = element(by.css('input#product-category-description'));
  imageFullInput: ElementFinder = element(by.css('input#file_imageFull'));
  imageFullUrlInput: ElementFinder = element(by.css('input#product-category-imageFullUrl'));
  imageThumbInput: ElementFinder = element(by.css('input#file_imageThumb'));
  imageThumbUrlInput: ElementFinder = element(by.css('input#product-category-imageThumbUrl'));
  shopSelect: ElementFinder = element(by.css('select#product-category-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setCategoryInput(category) {
    await this.categoryInput.sendKeys(category);
  }

  async getCategoryInput() {
    return this.categoryInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setImageFullInput(imageFull) {
    await this.imageFullInput.sendKeys(imageFull);
  }

  async getImageFullInput() {
    return this.imageFullInput.getAttribute('value');
  }

  async setImageFullUrlInput(imageFullUrl) {
    await this.imageFullUrlInput.sendKeys(imageFullUrl);
  }

  async getImageFullUrlInput() {
    return this.imageFullUrlInput.getAttribute('value');
  }

  async setImageThumbInput(imageThumb) {
    await this.imageThumbInput.sendKeys(imageThumb);
  }

  async getImageThumbInput() {
    return this.imageThumbInput.getAttribute('value');
  }

  async setImageThumbUrlInput(imageThumbUrl) {
    await this.imageThumbUrlInput.sendKeys(imageThumbUrl);
  }

  async getImageThumbUrlInput() {
    return this.imageThumbUrlInput.getAttribute('value');
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
