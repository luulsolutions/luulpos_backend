import { element, by, ElementFinder } from 'protractor';

export default class SystemEventsHistoryUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.systemEventsHistory.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  eventNameInput: ElementFinder = element(by.css('input#system-events-history-eventName'));
  eventDateInput: ElementFinder = element(by.css('input#system-events-history-eventDate'));
  eventApiInput: ElementFinder = element(by.css('input#system-events-history-eventApi'));
  eventNoteInput: ElementFinder = element(by.css('input#system-events-history-eventNote'));
  eventEntityNameInput: ElementFinder = element(by.css('input#system-events-history-eventEntityName'));
  eventEntityIdInput: ElementFinder = element(by.css('input#system-events-history-eventEntityId'));
  triggedBySelect: ElementFinder = element(by.css('select#system-events-history-triggedBy'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setEventNameInput(eventName) {
    await this.eventNameInput.sendKeys(eventName);
  }

  async getEventNameInput() {
    return this.eventNameInput.getAttribute('value');
  }

  async setEventDateInput(eventDate) {
    await this.eventDateInput.sendKeys(eventDate);
  }

  async getEventDateInput() {
    return this.eventDateInput.getAttribute('value');
  }

  async setEventApiInput(eventApi) {
    await this.eventApiInput.sendKeys(eventApi);
  }

  async getEventApiInput() {
    return this.eventApiInput.getAttribute('value');
  }

  async setEventNoteInput(eventNote) {
    await this.eventNoteInput.sendKeys(eventNote);
  }

  async getEventNoteInput() {
    return this.eventNoteInput.getAttribute('value');
  }

  async setEventEntityNameInput(eventEntityName) {
    await this.eventEntityNameInput.sendKeys(eventEntityName);
  }

  async getEventEntityNameInput() {
    return this.eventEntityNameInput.getAttribute('value');
  }

  async setEventEntityIdInput(eventEntityId) {
    await this.eventEntityIdInput.sendKeys(eventEntityId);
  }

  async getEventEntityIdInput() {
    return this.eventEntityIdInput.getAttribute('value');
  }

  async triggedBySelectLastOption() {
    await this.triggedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async triggedBySelectOption(option) {
    await this.triggedBySelect.sendKeys(option);
  }

  getTriggedBySelect() {
    return this.triggedBySelect;
  }

  async getTriggedBySelectedOption() {
    return this.triggedBySelect.element(by.css('option:checked')).getText();
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
