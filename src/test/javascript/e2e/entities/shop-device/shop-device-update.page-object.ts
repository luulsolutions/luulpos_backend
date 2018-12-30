import { element, by, ElementFinder } from 'protractor';

export default class ShopDeviceUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.shopDevice.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  deviceNameInput: ElementFinder = element(by.css('input#shop-device-deviceName'));
  deviceModelInput: ElementFinder = element(by.css('input#shop-device-deviceModel'));
  registeredDateInput: ElementFinder = element(by.css('input#shop-device-registeredDate'));
  shopSelect: ElementFinder = element(by.css('select#shop-device-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDeviceNameInput(deviceName) {
    await this.deviceNameInput.sendKeys(deviceName);
  }

  async getDeviceNameInput() {
    return this.deviceNameInput.getAttribute('value');
  }

  async setDeviceModelInput(deviceModel) {
    await this.deviceModelInput.sendKeys(deviceModel);
  }

  async getDeviceModelInput() {
    return this.deviceModelInput.getAttribute('value');
  }

  async setRegisteredDateInput(registeredDate) {
    await this.registeredDateInput.sendKeys(registeredDate);
  }

  async getRegisteredDateInput() {
    return this.registeredDateInput.getAttribute('value');
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
