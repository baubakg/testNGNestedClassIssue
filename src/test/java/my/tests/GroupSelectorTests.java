/**
 * MIT License
 *
 * © Copyright 2020 Adobe. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package my.tests;

import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import my.tests.data.nested.ClassContainer.NonGroupClass3;
import my.tests.data.normal.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GroupSelectorTests {

    @BeforeMethod
    public void resetProperty() {
        ExternalGroupManager.setGroupDefinitions(new HashMap<String, String[]>());
    }

    /**
     * Here we select a normal method as a test. OK
     *
     * Author : gandomi
     *
     *
     */
    @Test
    public void testNormalTestMethod() {

        ExternalGroupManager.addGroupDefinition("my.tests.data.normal.NonGroupClass1.step1", "extnest");

        // Rampup
        TestNG myTestNG = createTestNG();
        TestListenerAdapter tla = fetchTestResultsHandler(myTestNG);

        // Define suites
        XmlSuite mySuite = addSuitToTestNGTest(myTestNG, "Automated Suite External Group Checks Testing");

        // Add listeners
        mySuite.addListener("my.tests.ExternalGroupManager");

        // Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = attachTestToSuite(mySuite, "Test Simple External Group Checks Tests");
        myTest.addIncludedGroup("extnest");

        //Define packages
        List<XmlPackage> l_packages = new ArrayList<>();
        l_packages.add(new XmlPackage("my.tests.data.*"));
        myTest.setXmlPackages(l_packages);

        myTestNG.run();

        assertThat("We should have no failed methods", tla.getFailedTests().size(), is(equalTo(0)));

        assertThat("We should have no failed methods", tla.getPassedTests().size(), is(equalTo(1)));

        assertThat("We should have 1 successful methods",
                tla.getPassedTests().stream()
                        .filter(m -> m.getInstance().getClass().equals(NonGroupClass1.class))
                        .collect(Collectors.toList()).size(),
                is(equalTo(1)));
    }

    /**
     * Here we select a test class to be executed. OK
     *
     * Author : gandomi
     *
     *
     */
    @Test
    public void testNormalTestClass() {

        ExternalGroupManager.addGroupDefinition("my.tests.data.normal.NonGroupClass2", "extnest");

        // Rampup
        TestNG myTestNG = createTestNG();
        TestListenerAdapter tla = fetchTestResultsHandler(myTestNG);

        // Define suites
        XmlSuite mySuite = addSuitToTestNGTest(myTestNG, "Automated Suite External Group Checks Testing");

        // Add listeners
        mySuite.addListener("my.tests.ExternalGroupManager");

        // Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = attachTestToSuite(mySuite, "Test Simple External Group Checks Tests");
        myTest.addIncludedGroup("extnest");

        //Define packages
        List<XmlPackage> l_packages = new ArrayList<>();
        l_packages.add(new XmlPackage("my.tests.data.normal"));
        myTest.setXmlPackages(l_packages);

        myTestNG.run();

        assertThat("We should have no failed methods", tla.getFailedTests().size(), is(equalTo(0)));

        assertThat("We should have two successsful methods", tla.getPassedTests().size(), is(equalTo(2)));

        assertThat("We should have 1 successful methods",
                tla.getPassedTests().stream()
                        .filter(m -> m.getInstance().getClass().equals(NonGroupClass2.class))
                        .collect(Collectors.toList()).size(),
                is(equalTo(2)));
    }

    /**
     * Here we still select a Test Class to be executed, but the package
     * contains a Nested Test class (both without previously test groups) -
     * FAILS
     * <p>
     * The nested test class is also executed
     *
     * Author : gandomi
     *
     *
     */
    @Test
    public void testNormalTestClass_packageIncludesNestedTestClass() {

        ExternalGroupManager.addGroupDefinition("my.tests.data.normal.NonGroupClass2", "extnest");

        // Rampup
        TestNG myTestNG = createTestNG();
        TestListenerAdapter tla = fetchTestResultsHandler(myTestNG);

        // Define suites
        XmlSuite mySuite = addSuitToTestNGTest(myTestNG, "Automated Suite External Group Checks Testing");

        // Add listeners
        mySuite.addListener("my.tests.ExternalGroupManager");

        // Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = attachTestToSuite(mySuite, "Test Simple External Group Checks Tests");
        myTest.addIncludedGroup("extnest");

        //Define packages
        List<XmlPackage> l_packages = new ArrayList<>();
        l_packages.add(new XmlPackage("my.tests.data.*"));
        myTest.setXmlPackages(l_packages);

        myTestNG.run();

        assertThat("We should have no failed methods", tla.getFailedTests().size(), is(equalTo(0)));

        assertThat("We should have two successsful methods", tla.getPassedTests().size(), is(equalTo(2)));

        assertThat("We should have 1 successful methods",
                tla.getPassedTests().stream()
                        .filter(m -> m.getInstance().getClass().equals(NonGroupClass2.class))
                        .collect(Collectors.toList()).size(),
                is(equalTo(2)));
    }

    /**
     * Here we still select a Nested Test Class to be executed, but the package
     * contains a Normal Test class (both without previously test groups) -
     * FAILS
     * <p>
     * The normal test class is also executed
     * 
     * Author : gandomi
     *
     *
     */
    @Test
    public void testNestedTestClass_packageIncludesNormalTestClasses() {

        ExternalGroupManager.addGroupDefinition("my.tests.data.nested.ClassContainer$NonGroupClass3",
                "extnest");

        // Rampup
        TestNG myTestNG = createTestNG();
        TestListenerAdapter tla = fetchTestResultsHandler(myTestNG);

        // Define suites
        XmlSuite mySuite = addSuitToTestNGTest(myTestNG, "Automated Suite External Group Checks Testing");

        // Add listeners
        mySuite.addListener("my.tests.ExternalGroupManager");

        // Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = attachTestToSuite(mySuite, "Test Simple External Group Checks Tests");
        myTest.addIncludedGroup("extnest");

        //Define packages
        List<XmlPackage> l_packages = new ArrayList<>();
        l_packages.add(new XmlPackage("my.tests.data.normal"));
        l_packages.add(new XmlPackage("my.tests.data.nested"));

        myTest.setXmlPackages(l_packages);

        myTestNG.run();

        assertThat("We should have no failed methods", tla.getFailedTests().size(), is(equalTo(0)));

        assertThat("We should have 2 succesdul tests", tla.getPassedTests().size(), is(equalTo(2)));

        assertThat("We should have 1 successful methods",
                tla.getPassedTests().stream()
                        .filter(m -> m.getInstance().getClass().equals(NonGroupClass3.class))
                        .collect(Collectors.toList()).size(),
                is(equalTo(2)));
    }

    /**
     * In this case the package only contains a nested test class. OK
     *
     * Author : gandomi
     *
     *
     */
    @Test
    public void testNestedTestClass_allAlone() {

        ExternalGroupManager.addGroupDefinition("my.tests.data.nested.ClassContainer$NonGroupClass3",
                "extnest");

        // Rampup
        TestNG myTestNG = createTestNG();
        TestListenerAdapter tla = fetchTestResultsHandler(myTestNG);

        // Define suites
        XmlSuite mySuite = addSuitToTestNGTest(myTestNG, "Automated Suite External Group Checks Testing");

        // Add listeners
        mySuite.addListener("my.tests.ExternalGroupManager");

        // Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = attachTestToSuite(mySuite, "Test Simple External Group Checks Tests");
        myTest.addIncludedGroup("extnest");

        //Define packages
        List<XmlPackage> l_packages = new ArrayList<>();
        l_packages.add(new XmlPackage("my.tests.data.nested"));

        myTest.setXmlPackages(l_packages);

        myTestNG.run();

        assertThat("We should have no failed methods", tla.getFailedTests().size(), is(equalTo(0)));

        assertThat("We should have 2 succesdul tests", tla.getPassedTests().size(), is(equalTo(2)));

        assertThat("We should have 1 successful methods",
                tla.getPassedTests().stream()
                        .filter(m -> m.getInstance().getClass().equals(NonGroupClass3.class))
                        .collect(Collectors.toList()).size(),
                is(equalTo(2)));
    }

    /**
     * In this case our package contains a nested test class, but also a normal
     * est class. This time the test class already has groups. OK
     *
     * Author : gandomi
     *
     *
     */
    @Test
    public void testNestedTestClass_withTestContainingGroup() {

        ExternalGroupManager.addGroupDefinition("my.tests.data.nested.ClassContainer$NonGroupClass3",
                "extnest");

        // Rampup
        TestNG myTestNG = createTestNG();
        TestListenerAdapter tla = fetchTestResultsHandler(myTestNG);

        // Define suites
        XmlSuite mySuite = addSuitToTestNGTest(myTestNG, "Automated Suite External Group Checks Testing");

        // Add listeners
        mySuite.addListener("my.tests.ExternalGroupManager");

        // Create an instance of XmlTest and assign a name for it.
        XmlTest myTest = attachTestToSuite(mySuite, "Test Simple External Group Checks Tests");
        myTest.addIncludedGroup("extnest");

        //Define packages
        List<XmlPackage> l_packages = new ArrayList<>();
        l_packages.add(new XmlPackage("my.tests.data.nested"));
        l_packages.add(new XmlPackage("my.tests.data.withgroup"));

        myTest.setXmlPackages(l_packages);

        myTestNG.run();

        assertThat("We should have no failed methods", tla.getFailedTests().size(), is(equalTo(0)));

        assertThat("We should have 2 succesdul tests", tla.getPassedTests().size(), is(equalTo(2)));

        assertThat("We should have 1 successful methods",
                tla.getPassedTests().stream()
                        .filter(m -> m.getInstance().getClass().equals(NonGroupClass3.class))
                        .collect(Collectors.toList()).size(),
                is(equalTo(2)));
    }


    //////////////////  Helpers

    /**
     * This method creates a testng test instance with a result listener
     * 
     * @return a TestNG instance
     */
    public static TestNG createTestNG() {
        TestNG myTestNG = new TestNG();
        TestListenerAdapter tla = new TestListenerAdapter();
        myTestNG.addListener((ITestNGListener) tla);
        return myTestNG;
    }

    /**
     * Attached a newly created empty suite attached to the given testng
     * instance
     * 
     * @param in_testNGTestInstance
     * @param in_suiteName
     * @return
     */
    public static XmlSuite addSuitToTestNGTest(TestNG in_testNGTestInstance, String in_suiteName) {
        XmlSuite mySuite = new XmlSuite();
        mySuite.setName(in_suiteName);
        List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
        mySuites.add(mySuite);
        // Set the list of Suites to the testNG object you created earlier.
        in_testNGTestInstance.setXmlSuites(mySuites);
        return mySuite;
    }

    /**
     * Attaches a test to the TestNG suite
     * 
     * @param in_testNGSuite
     * @param in_testName
     * @return
     */
    public static XmlTest attachTestToSuite(XmlSuite in_testNGSuite, String in_testName) {
        XmlTest lr_Test = new XmlTest(in_testNGSuite);

        lr_Test.setName(in_testName);
        return lr_Test;
    }

    /**
     * This method returns the test result listener attached to the testng
     * instance
     * 
     * @param myTestNG
     * 
     * @return a TestListenerAdapter that listens on the test results
     */
    public static TestListenerAdapter fetchTestResultsHandler(TestNG myTestNG) {
        List<ITestListener> testLisenerAdapaters = myTestNG.getTestListeners();

        if (testLisenerAdapaters.size() != 1)
            throw new IllegalStateException("We did not expect to have more than one adapter");

        return (TestListenerAdapter) testLisenerAdapaters.get(0);
    }

}
