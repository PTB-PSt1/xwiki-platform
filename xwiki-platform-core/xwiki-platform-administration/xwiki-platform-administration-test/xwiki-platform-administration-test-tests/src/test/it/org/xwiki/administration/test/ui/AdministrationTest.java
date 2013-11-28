/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.administration.test.ui;

import org.junit.*;
import org.xwiki.administration.test.po.AdministrablePage;
import org.xwiki.administration.test.po.AdministrationPage;
import org.xwiki.test.ui.AbstractTest;
import org.xwiki.test.ui.SuperAdminAuthenticationRule;

/**
 * Verify the overall Administration application features.
 *
 * @version $Id$
 * @since 4.3M1
 */
public class AdministrationTest extends AbstractTest
{
    @Rule
    public SuperAdminAuthenticationRule authenticationRule = new SuperAdminAuthenticationRule(getUtil(), getDriver());

    /**
     * This method makes the following tests :
     *
     * <ul>
     * <li>Login as global admin.</li>
     * <li>Validate presence of default sections for global and space sections.</li>
     * <li>Validate presence of application administration sections at global level only.</li>
     * </ul>
     */
    @Test
    public void verifyGlobalAndSpaceSections()
    {
        // Go to any page. Note that we go to a not existing page for 2 reasons:
        // - verify that it has a menu action to administer the wiki
        // - (more importantly) it's faster than going to the wiki's home page which will take longer to display... ;)
        getUtil().gotoPage(getTestClassName(), getTestMethodName());
        AdministrablePage page = new AdministrablePage();
        AdministrationPage administrationPage = page.clickAdministerWiki();

        // TODO: Move these tests in their own modules, i.e. the modules that brought the Admin UI extension
        Assert.assertTrue(administrationPage.hasSection("Editing"));
        Assert.assertTrue(administrationPage.hasSection("Localization"));
        Assert.assertTrue(administrationPage.hasSection("Email"));
        Assert.assertTrue(administrationPage.hasSection("Presentation"));
        Assert.assertTrue(administrationPage.hasSection("Elements"));
        Assert.assertTrue(administrationPage.hasSection("Registration"));
        Assert.assertTrue(administrationPage.hasSection("Users"));
        Assert.assertTrue(administrationPage.hasSection("Groups"));
        Assert.assertTrue(administrationPage.hasSection("Rights"));
        Assert.assertTrue(administrationPage.hasSection("Registration"));
        Assert.assertTrue(administrationPage.hasSection("Import"));
        Assert.assertTrue(administrationPage.hasSection("Export"));
        Assert.assertTrue(administrationPage.hasSection("Templates"));

        // Select space administration (XWiki space, since that space exists)
        AdministrationPage spaceAdministrationPage = administrationPage.selectSpaceToAdminister("XWiki");

        // Note: I'm not sure this is good enough since waitUntilPageIsLoaded() tests for the existence of the footer
        // but if the page hasn't started reloading then the footer will be present... However I ran this test 300
        // times in a row without any failure...
        spaceAdministrationPage.waitUntilPageIsLoaded();

        Assert.assertTrue(spaceAdministrationPage.hasSection("Presentation"));
        Assert.assertTrue(spaceAdministrationPage.hasSection("Elements"));
        Assert.assertTrue(spaceAdministrationPage.hasSection("Rights"));

        // All those sections should not be present
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Editing"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Localization"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Email"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Registration"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Users"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Groups"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Registration"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Import"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Export"));
        Assert.assertTrue(spaceAdministrationPage.hasNotSection("Templates"));
    }
}
