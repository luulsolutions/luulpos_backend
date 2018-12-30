import { element, by, ElementFinder } from 'protractor';

export default class SuspensionHistoryUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.suspensionHistory.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  suspendedDateInput: ElementFinder = element(by.css('input#suspension-history-suspendedDate'));
  suspensionTypeSelect: ElementFinder = element(by.css('select#suspension-history-suspensionType'));
  suspendedReasonInput: ElementFinder = element(by.css('input#suspension-history-suspendedReason'));
  resolutionNoteInput: ElementFinder = element(by.css('input#suspension-history-resolutionNote'));
  unsuspensionDateInput: ElementFinder = element(by.css('input#suspension-history-unsuspensionDate'));
  profileSelect: ElementFinder = element(by.css('select#suspension-history-profile'));
  suspendedBySelect: ElementFinder = element(by.css('select#suspension-history-suspendedBy'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setSuspendedDateInput(suspendedDate) {
    await this.suspendedDateInput.sendKeys(suspendedDate);
  }

  async getSuspendedDateInput() {
    return this.suspendedDateInput.getAttribute('value');
  }

  async setSuspensionTypeSelect(suspensionType) {
    await this.suspensionTypeSelect.sendKeys(suspensionType);
  }

  async getSuspensionTypeSelect() {
    return this.suspensionTypeSelect.element(by.css('option:checked')).getText();
  }

  async suspensionTypeSelectLastOption() {
    await this.suspensionTypeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setSuspendedReasonInput(suspendedReason) {
    await this.suspendedReasonInput.sendKeys(suspendedReason);
  }

  async getSuspendedReasonInput() {
    return this.suspendedReasonInput.getAttribute('value');
  }

  async setResolutionNoteInput(resolutionNote) {
    await this.resolutionNoteInput.sendKeys(resolutionNote);
  }

  async getResolutionNoteInput() {
    return this.resolutionNoteInput.getAttribute('value');
  }

  async setUnsuspensionDateInput(unsuspensionDate) {
    await this.unsuspensionDateInput.sendKeys(unsuspensionDate);
  }

  async getUnsuspensionDateInput() {
    return this.unsuspensionDateInput.getAttribute('value');
  }

  async profileSelectLastOption() {
    await this.profileSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async profileSelectOption(option) {
    await this.profileSelect.sendKeys(option);
  }

  getProfileSelect() {
    return this.profileSelect;
  }

  async getProfileSelectedOption() {
    return this.profileSelect.element(by.css('option:checked')).getText();
  }

  async suspendedBySelectLastOption() {
    await this.suspendedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async suspendedBySelectOption(option) {
    await this.suspendedBySelect.sendKeys(option);
  }

  getSuspendedBySelect() {
    return this.suspendedBySelect;
  }

  async getSuspendedBySelectedOption() {
    return this.suspendedBySelect.element(by.css('option:checked')).getText();
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
