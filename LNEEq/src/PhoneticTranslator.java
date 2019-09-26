import java.util.*;
public class PhoneticTranslator {
    Scanner sc = new Scanner(System.in);

    boolean isVowel(char l){
        return (l == 'a' || l == 'e' || l == 'i' || l == 'o' || l == 'u');
    }

    boolean isFollowedByVowel(String[] cSequence, int i){
        if (i < cSequence.length - 1) {
            if (!isVowel(cSequence[i].charAt(0)) && !isVowel(cSequence[i + 1].charAt(0))) {
                return false;
            }
        } else {
            if (!isVowel(cSequence[i].charAt(0))){
                return false;
            }
        }
        return true;
    }

    String replaceVowel(String cs){
        String vowel;
        if (cs.equals("ee") || cs.equals("i") || cs.equals("oo")){
            vowel = "i";
        } else if (cs.equals("e") || cs.equals("u")){
            vowel = "e";
        } else {
            vowel = "a";
        }
        return vowel;
    }

    String replaceConsonant(String cs) {
        String consonant;
        if (cs.equals("b")) {
            consonant = "p";
        } else if (cs.equals("s") || cs.equals("th") || cs.equals("sh") || cs.equals("st") || cs.equals("d") || cs.equals("ch") || cs.equals("z")) {
            consonant = "t";
        } else if (cs.equals("k") || cs.equals("g")) {
            consonant = "cj";
        } else if (cs.equals("ng")) {
            consonant = "nj";
        } else if (cs.equals("f") || cs.equals("v") || cs.equals("h")) {
            consonant = "w";
        } else if (cs.equals("y")) {
            consonant = "j";
        } else {
            consonant = cs;
        }
        return consonant;
    }

    String[] insertVowel(String[] cSequence){
        for (int i = 0; i < cSequence.length; i++) {
            if (!isFollowedByVowel(cSequence,i)){
                cSequence[i] += "e";
            }
        }
        return cSequence;
    }

    void printTranslation(String[] charSequence){
        String output = "";
        for (String charS : charSequence){
            output += charS;
        }
        System.out.println(output);
    }

    String[] inputToArray(String input, String[] consPairs) {
        String[] charSequence = new String[input.length()];
        for (int i = 0; i < charSequence.length; i++){
            charSequence[i] = Character.toString(input.charAt(i));
        }

        for (int i = 0; i < charSequence.length-1; i++){
            if (i < charSequence.length-1) {
                if (isVowel(input.charAt(i))){
                    //Check if the next letter is also a vowel
                    if (isVowel(input.charAt(i + 1))){
                        charSequence[i] += Character.toString(input.charAt(i));
                        charSequence[i + 1] = "";
                        i++;
                    }
                }else {
                    //Check if the consonant forms a phonetic consonant pair
                    if (input.charAt(i) == input.charAt(i + 1)) {
                        charSequence[i + 1] = "";
                        i++;
                    } else {
                        for (String consPair : consPairs) {
                            String consP = input.charAt(i) + Character.toString(input.charAt(i + 1));
                            if (consP.equals(consPair)) {
                                charSequence[i] = consP;
                                charSequence[i + 1] = "";
                                i++;
                                break;
                            }
                        }
                    }
                }
            } else {
                charSequence[i] = Character.toString(input.charAt(i));
            }
        }

        int shortLength = 0;
        for (String cs : charSequence){
            if (cs != ""){
                shortLength++;
            }
        }

        String[] shortSequence = new String[shortLength];
        int csIndex = 0;
        for (int i = 0; i < shortSequence.length; i++){
            if (charSequence[csIndex] == "") {
                csIndex++;
            }
            shortSequence[i] = charSequence[csIndex];
            csIndex++;
        }

        return shortSequence;
    }

    String[] translate(String[] cSequence){
        for (int i = 0; i < cSequence.length; i++){
            if (isVowel(cSequence[i].charAt(0))){
                cSequence[i] = replaceVowel(cSequence[i]);
            } else {
                cSequence[i] = replaceConsonant(cSequence[i]);
            }

        }

        cSequence = insertVowel(cSequence).clone();

        if (isVowel(cSequence[0].charAt(0))){
            cSequence[0] = "j" + cSequence[0];
        }
        return cSequence;
    }

    void run(){
        System.out.println("Please input an english word or phrase, " +
                "spelled as phonetically as possible, silent letters. \n" +
                "Type \"help!c\" to recieve a spelling guide for consonants\n" +
                "Type \"help!v\" to recieve a spelling guide for vowels\n" +
                "Type any other word or phrase to begin translating\n" +
                "Type \"stop!\" to stop translating");

        String helpVowels = "Vowels (Sound, spelling):\n" +
                "Sounding like ɛ, è:    e\n" +             //Replaced by ɛ
                "   Eg. trap, dress\n" +
                "Sounding like e:       e\n" +             //Replaced by ɛ
                "   Eg. face\n" +
                "Sounding like ɑ, ah:   a\n" +             //Replaced by a
                "   Eg. bath\n" +
                "Sounding like ɔ, oh:   o\n" +             //Replaced by a
                "   Eg. lot, cloth, thought\n" +
                "Sounding like o:       o\n" +             //Replaced by a
                "   Eg. goat\n" +
                "Sounding like ɪ, ih:   i\n" +             //Replaced by i
                "   Eg. kit, years\n" +
                "Sounding like i:       i, ee\n" +         //Replaced by i
                "   Eg. fleece\n" +
                "Sounding like ʌ, uh:   u\n" +             //Replaced by e
                "   Eg. strut, gut\n" +
                "Sounding like u:       oo\n" +            //Replaced by i
                "   Eg. foot, goose\n" +
                "Sounding like aɪ, ɔɪ:  ai, oi resp.\n" +  //Replaced by a
                "   Eg. price, choice\n" +
                "Sounding like aʊ, ou:  ou\n" +            //Replaced by a
                "   Eg. mouth, sound";

        String helpConsonant = "Consonants:\n" +
                "Spell everything as close to a single letter sound as possible, so:\n" +
                "c becomes an s or k,\n" +
                "ph becomes an f,\n" +
                "wh in what becomes w, etc\n" +
                "the digraphs th, sh, ch, ng are allowed";

        String[] consPairs = {"sh","th","ch","ng","st"};
        String input;

        while (sc.hasNext()){
            input = sc.next().toLowerCase();
            if (input.equals("stop!"))  { return; }
            if (input.equals("help!v")) { System.out.println(helpVowels); }
            if (input.equals("help!c")) { System.out.println(helpConsonant); }

            String[] charSequence;

            charSequence = inputToArray(input,consPairs).clone();

            printTranslation(translate(charSequence));
        }
    }

    public static void main(String[] args) {
        new PhoneticTranslator().run();
    }
}
