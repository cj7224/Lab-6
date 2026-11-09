import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Zipf {
    public static String clean(String word) {
        word = word.replaceAll("[\\p{P}]", "");
        return word.toLowerCase(Locale.ROOT);
    }

    public static SelfOrganizingST count(ArrayList<String> words, boolean organize) {
        SelfOrganizingST st = new SelfOrganizingST(organize);
        for (String word : words) {
            Integer frequency = st.get(word);
            if (frequency == null) {
                st.put(word, 1);
            }
            else {
                st.put(word, frequency + 1);
            }
        }
        return st;
    }

    public static int largest(String[] words, int[] counts) {
        int largest = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] > counts[largest]) {
                largest = i;
            }
            else if (counts[i] == counts[largest]) {
                if (words[i].compareTo(words[largest]) < 0) {
                    largest = i;
                }
            }
        }
        return largest;
    }

    public static void printTop(SelfOrganizingST st) {
        String[] words = new String[st.size()];
        int[] counts = new int[st.size()];
        int i = 0;
        for (String word : st.keys()) {
            words[i] = word;
            counts[i] = st.get(word);
            i++;
        }
        for (i = 0; i < 50 && i < words.length; i++) {
            int index = largest(words, counts);
            System.out.println(words[index] + " " + counts[index]);
            counts[index] = -1;
        }
    }

    public static boolean test() {
        assert clean("Hello!").equals("hello");
        assert clean("DON'T").equals("dont");
        assert clean("sea-whale").equals("seawhale");
        assert clean("!!!").equals("");
        ArrayList<String> words = new ArrayList<String>();
        assert count(words, true).size() == 0;
        words.add("the");
        words.add("and");
        words.add("the");
        SelfOrganizingST st = count(words, true);
        assert st.size() == 2;
        assert st.get("the") == 2;
        assert st.get("and") == 1;
        String[] names = {"the", "and", "of"};
        int[] counts = {3, 3, 1};
        assert largest(names, counts) == 1;
        counts[1] = -1;
        assert largest(names, counts) == 0;
        return true;
    }

    public static void main(String[] args) {
        assert test();
        assert SelfOrganizingST.test();
        try {
            Scanner input;
            if (args.length == 0) {
                input = new Scanner(System.in, "UTF-8");
            }
            else {
                input = new Scanner(new File(args[0]), "UTF-8");
            }
            ArrayList<String> words = new ArrayList<String>();
            while (input.hasNext()) {
                String word = clean(input.next());
                if (word.matches(".*\\p{L}.*")) {
                    words.add(word);
                }
            }
            input.close();
            SelfOrganizingST st = count(words, true);
            if (args.length > 1 && args[1].equals("compare")) {
                SelfOrganizingST sequential = count(words, false);
                System.out.println("Sequential comparisons: " + sequential.comparisons());
                System.out.println("Self-organizing comparisons: " + st.comparisons());
                for (String word : st.keys()) {
                    assert st.get(word).equals(sequential.get(word));
                }
            }
            printTop(st);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }
}