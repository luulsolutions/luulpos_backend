import { element, by, ElementFinder } from 'protractor';

export default class ProductUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.product.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  productNameInput: ElementFinder = element(by.css('input#product-productName'));
  productDescriptionInput: ElementFinder = element(by.css('input#product-productDescription'));
  priceInput: ElementFinder = element(by.css('input#product-price'));
  quantityInput: ElementFinder = element(by.css('input#product-quantity'));
  productImageFullInput: ElementFinder = element(by.css('input#file_productImageFull'));
  productImageFullUrlInput: ElementFinder = element(by.css('input#product-productImageFullUrl'));
  productImageThumbInput: ElementFinder = element(by.css('input#file_productImageThumb'));
  productImageThumbUrlInput: ElementFinder = element(by.css('input#product-productImageThumbUrl'));
  dateCreatedInput: ElementFinder = element(by.css('input#product-dateCreated'));
  barcodeInput: ElementFinder = element(by.css('input#product-barcode'));
  serialCodeInput: ElementFinder = element(by.css('input#product-serialCode'));
  priorityPositionInput: ElementFinder = element(by.css('input#product-priorityPosition'));
  activeInput: ElementFinder = element(by.css('input#product-active'));
  isVariantProductInput: ElementFinder = element(by.css('input#product-isVariantProduct'));
  productTypesSelect: ElementFinder = element(by.css('select#product-productTypes'));
  shopSelect: ElementFinder = element(by.css('select#product-shop'));
  discountsSelect: ElementFinder = element(by.css('select#product-discounts'));
  taxesSelect: ElementFinder = element(by.css('select#product-taxes'));
  categorySelect: ElementFinder = element(by.css('select#product-category'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setProductNameInput(productName) {
    await this.productNameInput.sendKeys(productName);
  }

  async getProductNameInput() {
    return this.productNameInput.getAttribute('value');
  }

  async setProductDescriptionInput(productDescription) {
    await this.productDescriptionInput.sendKeys(productDescription);
  }

  async getProductDescriptionInput() {
    return this.productDescriptionInput.getAttribute('value');
  }

  async setPriceInput(price) {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput() {
    return this.priceInput.getAttribute('value');
  }

  async setQuantityInput(quantity) {
    await this.quantityInput.sendKeys(quantity);
  }

  async getQuantityInput() {
    return this.quantityInput.getAttribute('value');
  }

  async setProductImageFullInput(productImageFull) {
    await this.productImageFullInput.sendKeys(productImageFull);
  }

  async getProductImageFullInput() {
    return this.productImageFullInput.getAttribute('value');
  }

  async setProductImageFullUrlInput(productImageFullUrl) {
    await this.productImageFullUrlInput.sendKeys(productImageFullUrl);
  }

  async getProductImageFullUrlInput() {
    return this.productImageFullUrlInput.getAttribute('value');
  }

  async setProductImageThumbInput(productImageThumb) {
    await this.productImageThumbInput.sendKeys(productImageThumb);
  }

  async getProductImageThumbInput() {
    return this.productImageThumbInput.getAttribute('value');
  }

  async setProductImageThumbUrlInput(productImageThumbUrl) {
    await this.productImageThumbUrlInput.sendKeys(productImageThumbUrl);
  }

  async getProductImageThumbUrlInput() {
    return this.productImageThumbUrlInput.getAttribute('value');
  }

  async setDateCreatedInput(dateCreated) {
    await this.dateCreatedInput.sendKeys(dateCreated);
  }

  async getDateCreatedInput() {
    return this.dateCreatedInput.getAttribute('value');
  }

  async setBarcodeInput(barcode) {
    await this.barcodeInput.sendKeys(barcode);
  }

  async getBarcodeInput() {
    return this.barcodeInput.getAttribute('value');
  }

  async setSerialCodeInput(serialCode) {
    await this.serialCodeInput.sendKeys(serialCode);
  }

  async getSerialCodeInput() {
    return this.serialCodeInput.getAttribute('value');
  }

  async setPriorityPositionInput(priorityPosition) {
    await this.priorityPositionInput.sendKeys(priorityPosition);
  }

  async getPriorityPositionInput() {
    return this.priorityPositionInput.getAttribute('value');
  }

  getActiveInput() {
    return this.activeInput;
  }
  getIsVariantProductInput() {
    return this.isVariantProductInput;
  }
  async productTypesSelectLastOption() {
    await this.productTypesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async productTypesSelectOption(option) {
    await this.productTypesSelect.sendKeys(option);
  }

  getProductTypesSelect() {
    return this.productTypesSelect;
  }

  async getProductTypesSelectedOption() {
    return this.productTypesSelect.element(by.css('option:checked')).getText();
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

  async discountsSelectLastOption() {
    await this.discountsSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async discountsSelectOption(option) {
    await this.discountsSelect.sendKeys(option);
  }

  getDiscountsSelect() {
    return this.discountsSelect;
  }

  async getDiscountsSelectedOption() {
    return this.discountsSelect.element(by.css('option:checked')).getText();
  }

  async taxesSelectLastOption() {
    await this.taxesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async taxesSelectOption(option) {
    await this.taxesSelect.sendKeys(option);
  }

  getTaxesSelect() {
    return this.taxesSelect;
  }

  async getTaxesSelectedOption() {
    return this.taxesSelect.element(by.css('option:checked')).getText();
  }

  async categorySelectLastOption() {
    await this.categorySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async categorySelectOption(option) {
    await this.categorySelect.sendKeys(option);
  }

  getCategorySelect() {
    return this.categorySelect;
  }

  async getCategorySelectedOption() {
    return this.categorySelect.element(by.css('option:checked')).getText();
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
