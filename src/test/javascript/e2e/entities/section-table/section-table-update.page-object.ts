import { element, by, ElementFinder } from 'protractor';

export default class SectionTableUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.sectionTable.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  tableNumberInput: ElementFinder = element(by.css('input#section-table-tableNumber'));
  descriptionInput: ElementFinder = element(by.css('input#section-table-description'));
  shopSectionsSelect: ElementFinder = element(by.css('select#section-table-shopSections'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setTableNumberInput(tableNumber) {
    await this.tableNumberInput.sendKeys(tableNumber);
  }

  async getTableNumberInput() {
    return this.tableNumberInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async shopSectionsSelectLastOption() {
    await this.shopSectionsSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async shopSectionsSelectOption(option) {
    await this.shopSectionsSelect.sendKeys(option);
  }

  getShopSectionsSelect() {
    return this.shopSectionsSelect;
  }

  async getShopSectionsSelectedOption() {
    return this.shopSectionsSelect.element(by.css('option:checked')).getText();
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
