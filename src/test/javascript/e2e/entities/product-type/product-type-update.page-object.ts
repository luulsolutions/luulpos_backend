import { element, by, ElementFinder } from 'protractor';

export default class ProductTypeUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.productType.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  productTypeInput: ElementFinder = element(by.css('input#product-type-productType'));
  productTypeDescriptionInput: ElementFinder = element(by.css('input#product-type-productTypeDescription'));
  shopSelect: ElementFinder = element(by.css('select#product-type-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setProductTypeInput(productType) {
    await this.productTypeInput.sendKeys(productType);
  }

  async getProductTypeInput() {
    return this.productTypeInput.getAttribute('value');
  }

  async setProductTypeDescriptionInput(productTypeDescription) {
    await this.productTypeDescriptionInput.sendKeys(productTypeDescription);
  }

  async getProductTypeDescriptionInput() {
    return this.productTypeDescriptionInput.getAttribute('value');
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
