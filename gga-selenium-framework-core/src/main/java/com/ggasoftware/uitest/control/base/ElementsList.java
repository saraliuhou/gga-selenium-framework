package com.ggasoftware.uitest.control.base;

import com.ggasoftware.uitest.control.BaseElement;
import com.ggasoftware.uitest.control.interfaces.IList;
import com.ggasoftware.uitest.utils.common.Timer;
import com.ggasoftware.uitest.utils.map.MapArray;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.util.List;

import static com.ggasoftware.uitest.utils.TryCatchUtil.tryGetResult;
import static com.ggasoftware.uitest.utils.common.LinqUtils.*;
import static com.ggasoftware.uitest.utils.common.PrintUtils.print;
import static com.ggasoftware.uitest.utils.settings.FrameworkSettings.*;
import static java.lang.String.format;

/**
 * Created by Roman_Iovlev on 7/3/2015.
 */
public class ElementsList<TEnum extends Enum> extends BaseElement implements IList<TEnum> {
    public ElementsList() { }
    public ElementsList(By byLocator) { super(byLocator); }

    public List<WebElement> getWebElements() {
        return getWebElements(timeouts.waitElementSec);
    }

    public List<WebElement> getWebElements(int timeouInSec) {
        timeouts.currentTimoutSec = timeouInSec;
        List<WebElement> element = doJActionResult("Get web elements " + this.toString(), avatar::getElements);
        timeouts.currentTimoutSec = timeouts.waitElementSec;
        return element;
    }

    public boolean isDisplayed() { return waitDisplayed(0); }
    public boolean waitDisplayed() { return waitDisplayed(timeouts.waitElementSec); }
    public boolean waitDisplayed(int seconds) {
        setWaitTimeout(seconds);
        boolean result = new Timer(seconds*1000).wait(() -> where(getWebElements(), WebElement::isDisplayed).size() > 0);
        setWaitTimeout(timeouts.waitElementSec);
        return result;
    }

    public boolean waitVanished() { return waitDisplayed(timeouts.waitElementSec); }
    public boolean waitVanished(int seconds)  {
        setWaitTimeout(timeouts.retryMSec);
        boolean result = new Timer(seconds*1000).wait(() -> where(getWebElements(), WebElement::isDisplayed).size() == 0);
        setWaitTimeout(timeouts.waitElementSec);
        return result;
    }

    public WebElement getElement(String name)  {
        return first(getWebElements(), el -> el.getText().equals(name));
    }
    public WebElement getElement(int index) {
        return getWebElements().get(index);
    }
    public WebElement getElement(TEnum enumName) {
        return getElement(getEnumValue(enumName));
    }

    protected String getEnumValue(TEnum enumWithValue) {
        Field field;
        try { field = enumWithValue.getClass().getField("value");
            if (field.getType() != String.class)
                throw new Exception("Can't get Value from enum");
        } catch (Exception ex) { return enumWithValue.toString(); }
        return tryGetResult(() -> (String) field.get(enumWithValue));
    }

    protected MapArray<String, WebElement> getElementsAction() {
        try { return new MapArray<>(getWebElements(), WebElement::getText, value -> value);
        } catch (Exception ex) { asserter.exception(ex.getMessage()); return null; }
    }
    protected List<String> getLabelsAction() {
        return (List<String>) getElementsAction().keys();
    }

    public final MapArray<String, WebElement> getElements() {
        return doJActionResult("Get elements", this::getElementsAction);
    }
    public final List<String> getLabels() {
        return doJActionResult("Get names", this::getLabelsAction);
    }
    protected String getTextAction(WebElement element) { return element.getText(); }

    public final String getText(String name) {
        return doJActionResult(format("Get text for element '%s' with name '%s'", this.toString(), name),
            () -> getTextAction(getElement(name)));
    }

    public String getText(int index) {
        return doJActionResult(format("Get text for element '%s' with index '%s'", this.toString(), index),
                () -> getTextAction(getElement(index)));
    }
    public String getText(TEnum enumName) {
        return getText(getEnumValue(enumName));
    }
    public int count() {
        return getElements().size();
    }

    protected String getValueAction() { return print(select(getWebElements(), WebElement::getText)); }
    public final String getValue() { return doJActionResult("Get value", this::getValueAction); }
}
