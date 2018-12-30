import { element, by, ElementFinder } from 'protractor';

export default class EmployeeTimesheetUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.employeeTimesheet.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  checkinTimeInput: ElementFinder = element(by.css('input#employee-timesheet-checkinTime'));
  checkOutTimeInput: ElementFinder = element(by.css('input#employee-timesheet-checkOutTime'));
  regularHoursWorkedInput: ElementFinder = element(by.css('input#employee-timesheet-regularHoursWorked'));
  overTimeHoursWorkedInput: ElementFinder = element(by.css('input#employee-timesheet-overTimeHoursWorked'));
  payInput: ElementFinder = element(by.css('input#employee-timesheet-pay'));
  profileSelect: ElementFinder = element(by.css('select#employee-timesheet-profile'));
  shopSelect: ElementFinder = element(by.css('select#employee-timesheet-shop'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setCheckinTimeInput(checkinTime) {
    await this.checkinTimeInput.sendKeys(checkinTime);
  }

  async getCheckinTimeInput() {
    return this.checkinTimeInput.getAttribute('value');
  }

  async setCheckOutTimeInput(checkOutTime) {
    await this.checkOutTimeInput.sendKeys(checkOutTime);
  }

  async getCheckOutTimeInput() {
    return this.checkOutTimeInput.getAttribute('value');
  }

  async setRegularHoursWorkedInput(regularHoursWorked) {
    await this.regularHoursWorkedInput.sendKeys(regularHoursWorked);
  }

  async getRegularHoursWorkedInput() {
    return this.regularHoursWorkedInput.getAttribute('value');
  }

  async setOverTimeHoursWorkedInput(overTimeHoursWorked) {
    await this.overTimeHoursWorkedInput.sendKeys(overTimeHoursWorked);
  }

  async getOverTimeHoursWorkedInput() {
    return this.overTimeHoursWorkedInput.getAttribute('value');
  }

  async setPayInput(pay) {
    await this.payInput.sendKeys(pay);
  }

  async getPayInput() {
    return this.payInput.getAttribute('value');
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
