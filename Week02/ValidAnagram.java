import java.util.Arrays;

public class ValidAnagram {
    public boolean isAnagram(String s, String t) {

        char[] charArray = s.toCharArray();
        char[] array = t.toCharArray();
        Arrays.sort(charArray);
        Arrays.sort(array);
        return Arrays.equals(charArray,array);
    }
}
