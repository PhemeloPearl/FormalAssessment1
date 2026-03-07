package BasicTests;

import BasicBase.BaseTestWithWaits;
import BasicPages.LoginPage;
import BasicPages.DevicePurchasePage;
import org.testng.Assert;
import org.testng.annotations.Test;
import Utilities.ReadXSLdata;

public class LoginToNdosiWebsiteWithDataProvider extends BaseTestWithWaits {

    @Test(dataProvider = "testData", dataProviderClass = ReadXSLdata.class)
    public void loginToNdosiWebsite(String email, String password, String expectedWelcome,
                                    String deviceType, String brand, String storage,
                                    String color, String quantity, String address,
                                    String shipping, String warranty, String discountCode)
            throws InterruptedException {

        LoginPage loginPage   = new LoginPage(driver, wait);
        DevicePurchasePage devicePage = new DevicePurchasePage(driver, wait);

        //instruction 1
        loginPage.clickLoginNav();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginSubmit();
        loginPage.waitForLoginModalToClose();
        Assert.assertTrue(loginPage.getWelcomeHeadingText().contains(expectedWelcome), "Welcome message mismatch");

        //instruction 2: Navigate to Web tab
        loginPage.clickMenuButton();
        loginPage.clickPracticeLink();
        Assert.assertEquals(loginPage.getPracticeHeadingText(), expectedWelcome);
        loginPage.clickWebTab();
        Assert.assertTrue(devicePage.isInventoryFormDisplayed(),"Inventory Form should be visible");

        // instruction 3: Select Device Type
        devicePage.selectDeviceType(deviceType);
        Assert.assertTrue(devicePage.isBrandDropdownEnabled(),"Brand dropdown should be enabled");

        //instruction 4: Select Brand
        devicePage.selectBrand(brand);
        Assert.assertTrue(devicePage.isDevicePreviewDisplayed(), "Device preview should be visible");

        // instruction 5: Select Storage & verify unit price
        devicePage.selectStorage128GB();
        Assert.assertTrue(devicePage.isStorage128GBSelected(), "128GB should be selected");
        String unitPriceText = devicePage.getUnitPriceText();
        Assert.assertEquals(Double.parseDouble(unitPriceText.replace("R", "").replace(",", ".")), 480.00, "Unit price should be R480.00");
        double unitPrice = Double.parseDouble(unitPriceText.replace("R", "").replace(",", "."));

        // instruction 6: Select Color
        devicePage.selectColor(color);
        Assert.assertEquals(devicePage.getSelectedColor(), color, "Color mismatch");

        // instruction 7: Enter Quantity & verify subtotal
        devicePage.enterQuantity(quantity);
        int qty = devicePage.getQuantityValue();
        Assert.assertEquals(qty, Integer.parseInt(quantity), "Quantity mismatch");
        double expectedSubtotal = unitPrice * qty;
        Assert.assertEquals(Double.parseDouble(devicePage.getSubtotalText().replace("R", "").replace(",", ".")), expectedSubtotal, "Subtotal mismatch");

        // instruction 8: Enter Address
        devicePage.enterAddress(address);
        Assert.assertEquals(devicePage.getAddressValue(), address, "Address mismatch");

        // instruction 9: Click Next
        devicePage.clickNextButton();
        Assert.assertTrue(devicePage.isShippingOptionsDisplayed(), "Shipping options should be visible");

        // instruction 10: Select Shipping
        devicePage.selectShipping(shipping);

        // instruction 11: Select Warranty
        devicePage.selectWarranty(warranty);

        // instruction 12: Apply Discount & verify total
        devicePage.applyDiscountCode(discountCode);
        double shippingCost   = 25.00;
        double warrantyCost   = 49.00;
        double beforeDiscount = expectedSubtotal + shippingCost + warrantyCost;
        double expectedFinal  = Math.round((beforeDiscount - beforeDiscount * 0.10) * 100.0) / 100.0;
        String finalTotalText = devicePage.getFinalTotalText();
        Assert.assertEquals(Double.parseDouble(finalTotalText), expectedFinal, "Final total mismatch");

        // instruction 13: Confirm Purchase
        devicePage.clickConfirmPurchase();
        var toast = devicePage.getSuccessToast();
        Assert.assertTrue(devicePage.isToastVisible(toast) || toast.getText().length() > 0, "Success toast should appear");
        Assert.assertTrue(toast.getText().contains("your order was purchased successfully!"), "Toast message mismatch");

        // instruction 14: View Invoice History
        devicePage.clickViewInvoiceHistory();
        Assert.assertTrue(devicePage.isInvoiceCardDisplayed(), "Invoice card should be visible");

        // instruction 15: Verify Invoice Details
        devicePage.clickViewInvoice();
        Assert.assertEquals(devicePage.getInvoiceTotalText(), finalTotalText, "Invoice total mismatch");
        Assert.assertTrue(devicePage.getCustomerNameText().contains("Montjo Mohlake"), "Customer name mismatch");

        System.out.println("✅ Test passed! Invoice total: R" + devicePage.getInvoiceTotalText());

    }
}