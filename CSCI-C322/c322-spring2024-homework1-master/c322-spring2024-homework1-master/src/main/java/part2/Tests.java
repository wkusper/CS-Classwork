package part2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


class Tests {
    @Test
    void digits() {
        int[] nums = {1,2,3};
        int[] expected = {1,2,4};
        int[] actual = Problems.digits(nums);
        assertArrayEquals(expected, actual);

        int[] nums2 = {4,3,2,1};
        int[] expected2 = {4,3,2,2};
        int[] actual2 = Problems.digits(nums2);
        assertArrayEquals(expected2, actual2);

        int[] nums3 = {9};
        int[] expected3 = {1,0};
        int[] actual3 = Problems.digits(nums3);
        assertArrayEquals(expected3, actual3);
    }
    @Test
    void mergeSortedLists() {
        int[] nums1 = {1,2,4};
        int[] nums2 = {1,3,4};
        int[] expected = {1,1,2,3,4,4};
        int[] actual = Problems.mergeSortedLists(nums1, nums2);
        assertArrayEquals(expected, actual);

        int[] nums3 = {};
        int[] nums4 = {};
        int[] expected2 = {};
        int[] actual2 = Problems.mergeSortedLists(nums3, nums4);
        assertArrayEquals(expected2, actual2);

        int[] nums5 = {};
        int[] nums6 = {0};
        int[] expected3 = {0};
        int[] actual3 = Problems.mergeSortedLists(nums5, nums6);
        assertArrayEquals(expected3, actual3);
    }
    @Test
    void brackets() {
        String str = "()";
        boolean expected = true;
        boolean actual = Problems.brackets(str);
        assertEquals(expected, actual);

        String str2 = "()[]{}";
        boolean expected2 = true;
        boolean actual2 = Problems.brackets(str2);
        assertEquals(expected2, actual2);

        String str3 = "(]";
        boolean expected3 = false;
        boolean actual3 = Problems.brackets(str3);
        assertEquals(expected3, actual3);

        String str4 = "([)]";
        boolean expected4 = false;
        boolean actual4 = Problems.brackets(str4);
        assertEquals(expected4, actual4);

        String str5 = "{[]}";
        boolean expected5 = true;
        boolean actual5 = Problems.brackets(str5);
        assertEquals(expected5, actual5);
    }
    @Test
    void romanNumeral() {
        String str = "III";
        int expected = 3;
        int actual = Problems.romanNumeral(str);
        assertEquals(expected, actual);

        String str2 = "IV";
        int expected2 = 4;
        int actual2 = Problems.romanNumeral(str2);
        assertEquals(expected2, actual2);

        String str3 = "IX";
        int expected3 = 9;
        int actual3 = Problems.romanNumeral(str3);
        assertEquals(expected3, actual3);

        String str4 = "LVIII";
        int expected4 = 58;
        int actual4 = Problems.romanNumeral(str4);
        assertEquals(expected4, actual4);

        String str5 = "MCMXCIV";
        int expected5 = 1994;
        int actual5 = Problems.romanNumeral(str5);
        assertEquals(expected5, actual5);
    }
    @Test
    void commonPrefix() {
        String[] strs = {"flower","flow","flight"};
        String expected = "fl";
        String actual = Problems.commonPrefix(strs);
        assertEquals(expected, actual);

        String[] strs2 = {"dog","racecar","car"};
        String expected2 = "";
        String actual2 = Problems.commonPrefix(strs2);
        assertEquals(expected2, actual2);
    }
    @Test
    void palindrome() {
        int n = 121;
        boolean expected = true;
        boolean actual = Problems.palindrome(n);
        assertEquals(expected, actual);

        int n2 = -121;
        boolean expected2 = false;
        boolean actual2 = Problems.palindrome(n2);
        assertEquals(expected2, actual2);

        int n3 = 10;
        boolean expected3 = false;
        boolean actual3 = Problems.palindrome(n3);
        assertEquals(expected3, actual3);
    }
    @Test
    void twoSumTest() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] expected = {0,1};
        int[] actual = Problems.twoSum(nums, target);
        assertArrayEquals(expected, actual);

        int[] nums2 = {3, 2, 4};
        int target2 = 6;
        int[] expected2 = {1,2};
        int[] actual2 = Problems.twoSum(nums2, target2);
        assertArrayEquals(expected2, actual2);

        int[] nums3 = {3, 3};
        int target3 = 6;
        int[] expected3 = {0,1};
        int[] actual3 = Problems.twoSum(nums3, target3);
        assertArrayEquals(expected3, actual3);
    }
}