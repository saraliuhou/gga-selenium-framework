/****************************************************************************
 * Copyright (C) 2014 GGA Software Services LLC
 *
 * This file may be distributed and/or modified under the terms of the
 * GNU General Public License version 3 as published by the Free Software
 * Foundation.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 * WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 ***************************************************************************/
package com.ggasoftware.uitest.control.simple;

import com.ggasoftware.uitest.control.base.Clickable;
import com.ggasoftware.uitest.control.interfaces.IClickableText;
import com.ggasoftware.uitest.control.interfaces.IHaveValue;
import org.openqa.selenium.By;

/**
 * Button control implementation
 *
 * @author Alexeenko Yan
 */
public class Button extends Clickable implements IClickableText, IHaveValue {
    public Button() { }
    public Button(By byLocator) { super(byLocator); }

    protected String getTextAction() { return getWebElement().getText(); }
    protected String getValueAction() { return getTextAction(); }

    public final String getText() { return doJActionResult("Get text", this::getTextAction); }
    public final String getValue() { return doJActionResult("Get value", this::getTextAction); }

}
