/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import EmployeeTimesheetComponentsPage from './employee-timesheet.page-object';
import { EmployeeTimesheetDeleteDialog } from './employee-timesheet.page-object';
import EmployeeTimesheetUpdatePage from './employee-timesheet-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('EmployeeTimesheet e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let employeeTimesheetUpdatePage: EmployeeTimesheetUpdatePage;
  let employeeTimesheetComponentsPage: EmployeeTimesheetComponentsPage;
  let employeeTimesheetDeleteDialog: EmployeeTimesheetDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();

    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load EmployeeTimesheets', async () => {
    await navBarPage.getEntityPage('employee-timesheet');
    employeeTimesheetComponentsPage = new EmployeeTimesheetComponentsPage();
    expect(await employeeTimesheetComponentsPage.getTitle().getText()).to.match(/Employee Timesheets/);
  });

  it('should load create EmployeeTimesheet page', async () => {
    await employeeTimesheetComponentsPage.clickOnCreateButton();
    employeeTimesheetUpdatePage = new EmployeeTimesheetUpdatePage();
    expect(await employeeTimesheetUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /luulposApp.employeeTimesheet.home.createOrEditLabel/
    );
  });

  it('should create and save EmployeeTimesheets', async () => {
    const nbButtonsBeforeCreate = await employeeTimesheetComponentsPage.countDeleteButtons();

    await employeeTimesheetUpdatePage.setCheckinTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await employeeTimesheetUpdatePage.getCheckinTimeInput()).to.contain('2001-01-01T02:30');
    await employeeTimesheetUpdatePage.setCheckOutTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await employeeTimesheetUpdatePage.getCheckOutTimeInput()).to.contain('2001-01-01T02:30');
    await employeeTimesheetUpdatePage.setRegularHoursWorkedInput('5');
    expect(await employeeTimesheetUpdatePage.getRegularHoursWorkedInput()).to.eq('5');
    await employeeTimesheetUpdatePage.setOverTimeHoursWorkedInput('5');
    expect(await employeeTimesheetUpdatePage.getOverTimeHoursWorkedInput()).to.eq('5');
    await employeeTimesheetUpdatePage.setPayInput('5');
    expect(await employeeTimesheetUpdatePage.getPayInput()).to.eq('5');
    await employeeTimesheetUpdatePage.profileSelectLastOption();
    await employeeTimesheetUpdatePage.shopSelectLastOption();
    await waitUntilDisplayed(employeeTimesheetUpdatePage.getSaveButton());
    await employeeTimesheetUpdatePage.save();
    await waitUntilHidden(employeeTimesheetUpdatePage.getSaveButton());
    expect(await employeeTimesheetUpdatePage.getSaveButton().isPresent()).to.be.false;

    await employeeTimesheetComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await employeeTimesheetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last EmployeeTimesheet', async () => {
    await employeeTimesheetComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await employeeTimesheetComponentsPage.countDeleteButtons();
    await employeeTimesheetComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    employeeTimesheetDeleteDialog = new EmployeeTimesheetDeleteDialog();
    expect(await employeeTimesheetDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /luulposApp.employeeTimesheet.delete.question/
    );
    await employeeTimesheetDeleteDialog.clickOnConfirmButton();

    await employeeTimesheetComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await employeeTimesheetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
