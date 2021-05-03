
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Please use StandardIntegroTestNGAnnotationTransformers instead.
 * 
 * @author gandomi
 *
 */
public class ExternalGroupManager implements IAnnotationTransformer {

    private static Map<String, String[]> groupDefinitions = new HashMap<>();

    public static Map<String, String[]> getGroupDefinitions() {
        return groupDefinitions;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
            Method testMethod) {

        String l_fullName = null;

        if (testMethod != null) {
            l_fullName = testMethod.getDeclaringClass().getTypeName() + "." + testMethod.getName();
        } else if (testClass != null) {

            l_fullName = testClass.getTypeName();
        }

        if (l_fullName != null) {
            // If added group map is set
            if (groupDefinitions.containsKey(l_fullName)) {
                String[] fullGroup = mergeStringArrays(annotation.getGroups(),
                        groupDefinitions.get(l_fullName));
                annotation.setGroups(fullGroup);
            } else {
                String[] fullGroup = mergeStringArrays(annotation.getGroups(),
                        new String[] { "egm-notselected" });
                annotation.setGroups(fullGroup);
            }
        }

    }

    /**
     * Merges two array strings
     * 
     * Author : gandomi
     *
     * @param in_straingArray1
     * @param in_straingArray2
     * @return
     */
    private static String[] mergeStringArrays(String[] in_straingArray1, String[] in_straingArray2) {
        return Stream.concat(Arrays.stream(in_straingArray1), Arrays.stream(in_straingArray2))
                .toArray(String[]::new);
    }

    /**
     * Adds a group to a test definition. If the test is not defined we both
     * store the test and its group.
     * 
     * Author : gandomi
     *
     * @param in_methodFullName
     *        The full qualified name of the test we want to enrich
     * @param in_groupName
     *        The name of the group we want to add
     */
    public static void addGroupDefinition(String in_methodFullName, String in_groupName) {
        // Transform to Sting[]
        String[] l_groupArray = { in_groupName };

        if (getGroupDefinitions().containsKey(in_methodFullName)) {
            Set<String> l_originalGroups = new HashSet<String>(
                    Arrays.asList(getGroupDefinitions().get(in_methodFullName)));
            l_originalGroups.add(in_groupName);
            getGroupDefinitions().put(in_methodFullName,
                    l_originalGroups.toArray(new String[l_originalGroups.size()]));
        } else {
            getGroupDefinitions().put(in_methodFullName, l_groupArray);
        }

    }

    public static void setGroupDefinitions(Map<String, String[]> groupDefinitions) {
        ExternalGroupManager.groupDefinitions = groupDefinitions;
    }

}
