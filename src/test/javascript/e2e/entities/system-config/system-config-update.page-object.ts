import { element, by, ElementFinder } from 'protractor';

export default class SystemConfigUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.systemConfig.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  keyInput: ElementFinder = element(by.css('input#system-config-key'));
  valueInput: ElementFinder = element(by.css('input#system-config-value'));
  configurationTypeSelect: ElementFinder = element(by.css('select#system-config-configurationType'));
  noteInput: ElementFinder = element(by.css('input#system-config-note'));
  enabledInput: ElementFinder = element(by.css('input#system-config-enabled'));
  shopSelect: ElementFinder = element(by.css('select#system-config-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setKeyInput(key) {
    await this.keyInput.sendKeys(key);
  }

  async getKeyInput() {
    return this.keyInput.getAttribute('value');
  }

  async setValueInput(value) {
    await this.valueInput.sendKeys(value);
  }

  async getValueInput() {
    return this.valueInput.getAttribute('value');
  }

  async setConfigurationTypeSelect(configurationType) {
    await this.configurationTypeSelect.sendKeys(configurationType);
  }

  async getConfigurationTypeSelect() {
    return this.configurationTypeSelect.element(by.css('option:checked')).getText();
  }

  async configurationTypeSelectLastOption() {
    await this.configurationTypeSelect
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

  getEnabledInput() {
    return this.enabledInput;
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
