package BasicTests;

import BasicBase.BaseTestWithWaits;
import BasicPages.DevicePurchaseWithWaits;
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

        DevicePurchaseWithWaits page = new DevicePurchaseWithWaits(driver, wait);

        // STEP 1: Login
        page.clickLoginNav();
        page.enterEmail(email);
        page.enterPassword(password);
        page.clickLoginSubmit();
        page.waitForLoginModalToClose();
        Assert.assertTrue(page.getWelcomeHeadingText().contains(expectedWelcome),
                "Welcome message mismatch");

        // STEP 2: Navigate to Web Automation Advance tab
        page.clickMenuButton();
        page.clickPracticeLink();
        Assert.assertEquals(page.getPracticeHeadingText(), expectedWelcome);
        page.clickWebTab();
        Assert.assertTrue(page.isInventoryFormDisplayed(), "Inventory Form should be visible");

        // STEP 3: Select Device Type
        page.selectDeviceType(deviceType);
        Assert.assertTrue(page.isBrandDropdownEnabled(), "Brand dropdown should be enabled");

        // STEP 4: Select Brand
        page.selectBrand(brand);
        Assert.assertTrue(page.isDevicePreviewDisplayed(), "Device preview should be visible");

        // STEP 5: Select Storage & verify unit price
        page.selectStorage128GB();
        Assert.assertTrue(page.isStorage128GBSelected(), "128GB should be selected");
        String unitPriceText = page.getUnitPriceText();
        Assert.assertEquals(unitPriceText, "R480.00", "Unit price should be R480.00");
        double unitPrice = Double.parseDouble(unitPriceText.replace("R", ""));

        // STEP 6: Select Color
        page.selectColor(color);
        Assert.assertEquals(page.getSelectedColor(), color, "Color should be " + color);

        // STEP 7: Enter Quantity & verify subtotal
        page.enterQuantity(quantity);
        int qty = page.getQuantityValue();
        Assert.assertEquals(qty, Integer.parseInt(quantity), "Quantity mismatch");
        double expectedSubtotal = unitPrice * qty;
        String expectedSubtotalText = page.getSubtotalText();
        Assert.assertEquals(
                Double.parseDouble(expectedSubtotalText.replace("R", "").replace(",", ".")),
                expectedSubtotal,
                "Subtotal mismatch");

        // STEP 8: Enter Address
        page.enterAddress(address);
        Assert.assertEquals(page.getAddressValue(), address, "Address mismatch");

        // STEP 9: Click Next
        page.clickNextButton();
        Assert.assertTrue(page.isShippingOptionsDisplayed(), "Shipping options should be visible");

        // STEP 10: Select Shipping
        page.selectShipping(shipping);

        // STEP 11: Select Warranty
        page.selectWarranty(warranty);

        // STEP 12: Apply Discount & verify final total
        page.applyDiscountCode(discountCode);
        double shippingCost  = 25.00;
        double warrantyCost  = 49.00;
        double beforeDiscount = expectedSubtotal + shippingCost + warrantyCost;
        double expectedFinal  = Math.round((beforeDiscount - beforeDiscount * 0.10) * 100.0) / 100.0;
        String finalTotalText = page.getFinalTotalText();
        Assert.assertEquals(Double.parseDouble(finalTotalText), expectedFinal, "Final total mismatch");

        // STEP 13: Confirm Purchase
        page.clickConfirmPurchase();
        var toast = page.getSuccessToast();
        Assert.assertTrue(page.isToastVisible(toast) || toast.getText().length() > 0,
                "Success toast should appear");
        Assert.assertTrue(toast.getText().contains("your order was purchased successfully!"),
                "Toast message mismatch");

        // STEP 14: View Invoice History
        page.clickViewInvoiceHistory();
        Assert.assertTrue(page.isInvoiceCardDisplayed(), "Invoice card should be visible");

        // STEP 15: View Invoice & verify details
        page.clickViewInvoice();
        Assert.assertEquals(page.getInvoiceTotalText(), finalTotalText, "Invoice total mismatch");
        Assert.assertTrue(page.getCustomerNameText().contains("Montjo Mohlake"),
                "Customer name mismatch");

        System.out.println("✅ Test passed! Invoice total: R" + page.getInvoiceTotalText());
    }
}