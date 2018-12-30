import { element, by, ElementFinder } from 'protractor';

import { waitUntilCount, waitUntilDisplayed } from '../../util/utils';

export default class ShopSectionComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('shop-section-heading'));

  async clickOnCreateButton() {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton() {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  getTitle() {
    return this.title;
  }

  async waitUntilLoaded() {
    await waitUntilDisplayed(this.deleteButtons.first());
  }

  async waitUntilDeleteButtonsLength(length) {
    await waitUntilCount(this.deleteButtons, length);
  }
}

export class ShopSectionDeleteDialog {
  private dialogTitle: ElementFinder = element(by.id('luulposApp.shopSection.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-shopSection'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
