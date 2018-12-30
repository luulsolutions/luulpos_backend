import { element, by, ElementFinder } from 'protractor';

export default class EmailBalancerUpdatePage {
  pageTitle: ElementFinder = element(by.id('luulposApp.emailBalancer.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  relayIdInput: ElementFinder = element(by.css('input#email-balancer-relayId'));
  relayPasswordInput: ElementFinder = element(by.css('input#email-balancer-relayPassword'));
  startingCountInput: ElementFinder = element(by.css('input#email-balancer-startingCount'));
  endingCountInput: ElementFinder = element(by.css('input#email-balancer-endingCount'));
  providerInput: ElementFinder = element(by.css('input#email-balancer-provider'));
  relayPortInput: ElementFinder = element(by.css('input#email-balancer-relayPort'));
  enabledInput: ElementFinder = element(by.css('input#email-balancer-enabled'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setRelayIdInput(relayId) {
    await this.relayIdInput.sendKeys(relayId);
  }

  async getRelayIdInput() {
    return this.relayIdInput.getAttribute('value');
  }

  async setRelayPasswordInput(relayPassword) {
    await this.relayPasswordInput.sendKeys(relayPassword);
  }

  async getRelayPasswordInput() {
    return this.relayPasswordInput.getAttribute('value');
  }

  async setStartingCountInput(startingCount) {
    await this.startingCountInput.sendKeys(startingCount);
  }

  async getStartingCountInput() {
    return this.startingCountInput.getAttribute('value');
  }

  async setEndingCountInput(endingCount) {
    await this.endingCountInput.sendKeys(endingCount);
  }

  async getEndingCountInput() {
    return this.endingCountInput.getAttribute('value');
  }

  async setProviderInput(provider) {
    await this.providerInput.sendKeys(provider);
  }

  async getProviderInput() {
    return this.providerInput.getAttribute('value');
  }

  async setRelayPortInput(relayPort) {
    await this.relayPortInput.sendKeys(relayPort);
  }

  async getRelayPortInput() {
    return this.relayPortInput.getAttribute('value');
  }

  getEnabledInput() {
    return this.enabledInput;
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
