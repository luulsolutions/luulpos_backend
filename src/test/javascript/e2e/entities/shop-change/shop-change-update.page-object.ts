import { element, by, ElementFinder } from 'protractor';

export default class ShopChangeUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.shopChange.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  changeInput: ElementFinder = element(by.css('input#shop-change-change'));
  changedEntityInput: ElementFinder = element(by.css('input#shop-change-changedEntity'));
  noteInput: ElementFinder = element(by.css('input#shop-change-note'));
  changeDateInput: ElementFinder = element(by.css('input#shop-change-changeDate'));
  shopSelect: ElementFinder = element(by.css('select#shop-change-shop'));
  changedBySelect: ElementFinder = element(by.css('select#shop-change-changedBy'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setChangeInput(change) {
    await this.changeInput.sendKeys(change);
  }

  async getChangeInput() {
    return this.changeInput.getAttribute('value');
  }

  async setChangedEntityInput(changedEntity) {
    await this.changedEntityInput.sendKeys(changedEntity);
  }

  async getChangedEntityInput() {
    return this.changedEntityInput.getAttribute('value');
  }

  async setNoteInput(note) {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput() {
    return this.noteInput.getAttribute('value');
  }

  async setChangeDateInput(changeDate) {
    await this.changeDateInput.sendKeys(changeDate);
  }

  async getChangeDateInput() {
    return this.changeDateInput.getAttribute('value');
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

  async changedBySelectLastOption() {
    await this.changedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async changedBySelectOption(option) {
    await this.changedBySelect.sendKeys(option);
  }

  getChangedBySelect() {
    return this.changedBySelect;
  }

  async getChangedBySelectedOption() {
    return this.changedBySelect.element(by.css('option:checked')).getText();
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
