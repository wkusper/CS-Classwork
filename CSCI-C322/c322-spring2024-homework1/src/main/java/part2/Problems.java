package part2;

import java.util.Stack;

public class Problems {
    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for(int i = 0; i < nums.length; i++) {
            for(int j = i + 1; j < nums.length; j++) {
                if(nums[i] + nums[j] == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }
    public static boolean palindrome(int n) {
        String s = Integer.toString(n);
        int i = 0;
        int j = s.length() - 1;
        while(i < j) {
            if(s.charAt(i) != s.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }
    public static String commonPrefix(String[] strs) {
        String result = "";
        if(strs.length == 0) {
            return result;
        }
        for(int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for(int j = 1; j < strs.length; j++) {
                if(i >= strs[j].length() || strs[j].charAt(i) != c) {
                    return result;
                }
            }
            result += c;
        }
        return result;
    }
    public static int romanNumeral(String str) {
        int result = 0;
        int prev = 0;
        int current = 0;
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch(c) {
                case 'I':
                    current = 1;
                    break;
                case 'V':
                    current = 5;
                    break;
                case 'X':
                    current = 10;
                    break;
                case 'L':
                    current = 50;
                    break;
                case 'C':
                    current = 100;
                    break;
                case 'D':
                    current = 500;
                    break;
                case 'M':
                    current = 1000;
                    break;
            }
            if (prev == 0) {
                result += current;
                prev = current;
            }
            else if (prev < current) {
                result = result - prev + (current - prev);
                prev = current;
            }
            else {
                result += current;
                prev = current;
            }
        }
        return result;
    }
    public static boolean brackets(String str) {
        Stack<Character> st=new Stack<Character>();
        for(int i=0;i<str.length();i++){

            if(str.charAt(i)=='(' || str.charAt(i)=='{' || str.charAt(i)=='['){
                st.push(str.charAt(i));
            }
            else if(st.empty()){
                return false;
            }
            // Check for the close brackets pair on the top and pop them
            else if (st.peek() == '{' && str.charAt(i) == '}' ) {
                st.pop();
            } else if (st.peek() == '(' && str.charAt(i) == ')' ) {
                st.pop();
            } else if (st.peek() == '[' && str.charAt(i) == ']' ) {
                st.pop();
            }else{
                return false;
            }
        }
        return st.isEmpty();
    }
    public static int[] mergeSortedLists(int[] nums1, int[] nums2) {
        int[] result = new int[nums1.length + nums2.length];
        int i = 0;
        int j = 0;
        while(i < nums1.length && j < nums2.length) {
            if(nums1[i] < nums2[j]) {
                result[i + j] = nums1[i];
                i++;
            }
            else {
                result[i + j] = nums2[j];
                j++;
            }
        }
        while(i < nums1.length) {
            result[i + j] = nums1[i];
            i++;
        }
        while(j < nums2.length) {
            result[i + j] = nums2[j];
            j++;
        }
        return result;
    }
    public static int[] digits(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }

        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }
}
