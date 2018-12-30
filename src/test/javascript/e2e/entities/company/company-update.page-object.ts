import { element, by, ElementFinder } from 'protractor';

export default class CompanyUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.company.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  companyNameInput: ElementFinder = element(by.css('input#company-companyName'));
  descriptionInput: ElementFinder = element(by.css('input#company-description'));
  noteInput: ElementFinder = element(by.css('input#company-note'));
  companyLogoInput: ElementFinder = element(by.css('input#file_companyLogo'));
  companyLogoUrlInput: ElementFinder = element(by.css('input#company-companyLogoUrl'));
  activeInput: ElementFinder = element(by.css('input#company-active'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setCompanyNameInput(companyName) {
    await this.companyNameInput.sendKeys(companyName);
  }

  async getCompanyNameInput() {
    return this.companyNameInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setNoteInput(note) {
    await this.noteInput.sendKeys(note);
  }

  async getNoteInput() {
    return this.noteInput.getAttribute('value');
  }

  async setCompanyLogoInput(companyLogo) {
    await this.companyLogoInput.sendKeys(companyLogo);
  }

  async getCompanyLogoInput() {
    return this.companyLogoInput.getAttribute('value');
  }

  async setCompanyLogoUrlInput(companyLogoUrl) {
    await this.companyLogoUrlInput.sendKeys(companyLogoUrl);
  }

  async getCompanyLogoUrlInput() {
    return this.companyLogoUrlInput.getAttribute('value');
  }

  getActiveInput() {
    return this.activeInput;
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
