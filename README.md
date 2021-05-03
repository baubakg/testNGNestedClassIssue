# testNGNestedClassIssue
An example project showing how selecting nested classes for executions can be problematic.

The origin of this project is to externalize test group definitions.

In short we have an AnnotationTransformer that defines groups based on a predefined map of <Method Name, [Groups]> . The code it self works just fine as long as:
* The tests are defined on the method
* The tests are defined on a simple Class

## Tests

### 1. my.tests.GroupSelectorTests.testNormalTestMethod() - OK
A simple definition on a test method

**Expected:** only the following test method should be executed :
my.tests.data.NonGroupClass1.step1

**Result:** OK

### 2. my.tests.GroupSelectorTests.testNormalTestClass() - OK
A simple definition of groups on a test class

**Expected:** only test methods from the class :
my.tests.data.normal.NonGroupClass2

**Result:** OK

### 3. my.tests.GroupSelectorTests.testNormalTestClass_packageIncludesNestedTestClass() - FAIL
In this case the test package includes a Test Class nested in a normal class. However we are selecting the same class as in case 2. Just by the existence of the Nested TestClass the whole selections goes bad.

**Expected:** only test methods from the class :
my.tests.data.normal.NonGroupClass2

**Result:** We have more included tests (Also included were results from my.tests.dataWithNested.ClassContainerNonGroupClass3) FAIL

### 4. my.tests.GroupSelectorTests.testNestedTestClass_packageIncludesNormalTestClasses() - FAIL
In this case the test package includes a Test Class nested in a normal class. In this case we selected the nested class. Just by the existence of the Nested TestClass the whole selections goes bad.

**Expected:** only test methods from the class :
my.tests.data.nested.ClassContainer$NonGroupClass3

**Result 6.14.3 :** No tests are executed at all. The test class my.tests.data.nested.ClassContainer$NonGroupClass3 does not even show up in the transform method for its class
**Result 7.x :** We not only execute my.tests.data.nested.ClassContainer$NonGroupClass3 , but also the class my.tests.data.normal.NonGroupClass2 is also executed, even though it was not selected.


### 5. my.tests.GroupSelectorTests.testNestedTestClass_allAlone() - OK 
In this case we just execute the nested class alone.

**Expected:** only test methods from the class :
my.tests.dataWithNested.ClassContainer$NonGroupClass3

**Result:** The nested test class is selected found and executed OK

### 6. my.tests.GroupSelectorTests.testNestedTestClass_withTestContainingGroup() - OK 
We discovered that the problem is mitigated is all classes/tests have groups assigned to them. In this example the selected packages includes a est class with group definitions. In this case the test works perfectly.

**Expected:** only test methods from the class :
my.tests.dataWithNested.ClassContainer$NonGroupClass3

**Result:** The nested test class is selected found and executed OK

