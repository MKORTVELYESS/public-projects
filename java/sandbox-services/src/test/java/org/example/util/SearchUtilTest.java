package org.example.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

class SearchUtilTest {
  static List<String> fistNames =
      List.of(
          "aaron",
          "abby",
          "adam",
          "adrian",
          "albert",
          "alex",
          "alexis",
          "alice",
          "alina",
          "allen",
          "alyssa",
          "amber",
          "amy",
          "andrew",
          "andy",
          "angela",
          "anna",
          "annie",
          "anthony",
          "april",
          "ashley",
          "austin",
          "ava",
          "barbara",
          "barry",
          "ben",
          "benjamin",
          "bernice",
          "bill",
          "bobby",
          "brad",
          "bradley",
          "brandon",
          "brenda",
          "brian",
          "brianna",
          "brittany",
          "brooke",
          "bruce",
          "bryan",
          "cameron",
          "candice",
          "carl",
          "carmen",
          "carol",
          "caroline",
          "carrie",
          "catherine",
          "cecil",
          "celine",
          "charles",
          "charlie",
          "chloe",
          "chris",
          "christian",
          "christina",
          "christine",
          "cindy",
          "claire",
          "clara",
          "clark",
          "claudia",
          "clayton",
          "colin",
          "colleen",
          "connie",
          "corey",
          "courtney",
          "craig",
          "crystal",
          "cynthia",
          "dan",
          "dana",
          "daniel",
          "danielle",
          "danny",
          "darlene",
          "dave",
          "david",
          "dean",
          "debbie",
          "deborah",
          "denise",
          "dennis",
          "derek",
          "diana",
          "diane",
          "don",
          "donald",
          "doris",
          "doug",
          "douglas",
          "drew",
          "duane",
          "dylan",
          "earl",
          "ed",
          "eddie",
          "edgar",
          "edward",
          "edwin",
          "elaine",
          "eleanor",
          "eli",
          "elijah",
          "elizabeth",
          "ella",
          "ellen",
          "emily",
          "emma",
          "eric",
          "erica",
          "erin",
          "ernest",
          "ethan",
          "eva",
          "evelyn",
          "evan",
          "faith",
          "felicia",
          "felix",
          "fiona",
          "florence",
          "frances",
          "francis",
          "frank",
          "gabriel",
          "gabriella",
          "gary",
          "gene",
          "geoff",
          "george",
          "georgia",
          "gerald",
          "gina",
          "gladys",
          "gloria",
          "gordon",
          "grace",
          "greg",
          "gregory",
          "gretchen",
          "gus",
          "gwen",
          "hannah",
          "harold",
          "harry",
          "heather",
          "heidi",
          "helen",
          "henry",
          "holly",
          "howard",
          "ian",
          "ida",
          "irene",
          "isaac",
          "isabel",
          "ivan",
          "jack",
          "jackie",
          "jacob",
          "jade",
          "jake",
          "james",
          "jamie",
          "jan",
          "jane",
          "janet",
          "janice",
          "jared",
          "jason",
          "jay",
          "jean",
          "jeff",
          "jeffrey",
          "jen",
          "jenna",
          "jennifer",
          "jenny",
          "jeremy",
          "jerry",
          "jesse",
          "jessica",
          "jim",
          "jimmy",
          "joan",
          "joanna",
          "joanne",
          "jodi",
          "joe",
          "joel",
          "john",
          "johnny",
          "jon",
          "jonathan",
          "jordan",
          "jorge",
          "jose",
          "joseph",
          "josh",
          "joshua",
          "joy",
          "joyce",
          "juan",
          "judith",
          "judy",
          "julia",
          "julian",
          "julie",
          "justin",
          "karen",
          "karl",
          "kate",
          "katelyn",
          "kathleen",
          "kathryn",
          "kathy",
          "katie",
          "kayla",
          "keith",
          "kelly",
          "ken",
          "kenneth",
          "kevin",
          "kim",
          "kirk",
          "kristen",
          "kristin",
          "kyle",
          "laura",
          "lauren",
          "lawrence",
          "leah",
          "lee",
          "leo",
          "leon",
          "leonard",
          "linda",
          "lindsay",
          "lisa",
          "logan",
          "lori",
          "louis",
          "lucas",
          "lucy",
          "luke",
          "lydia",
          "lynn",
          "madeline",
          "madison",
          "manuel",
          "marc",
          "margaret",
          "maria",
          "marie",
          "marilyn",
          "mark",
          "marlene",
          "marshall");

  private static List<String> generateInputs(int count) {

    List<String> lastNames =
        List.of(
            "adams",
            "allen",
            "anderson",
            "armstrong",
            "arnold",
            "austin",
            "bailey",
            "baker",
            "barnes",
            "barnett",
            "barrett",
            "beck",
            "bell",
            "bennett",
            "bishop",
            "black",
            "blair",
            "boone",
            "boyd",
            "bradley",
            "brady",
            "brewer",
            "brooks",
            "brown",
            "bryant",
            "burke",
            "burns",
            "burton",
            "bush",
            "butler",
            "byrd",
            "cameron",
            "campbell",
            "carpenter",
            "carr",
            "carroll",
            "carter",
            "casey",
            "castro",
            "chambers",
            "chan",
            "chapman",
            "clark",
            "clarke",
            "clayton",
            "clements",
            "cohen",
            "cole",
            "coleman",
            "collins",
            "combs",
            "conner",
            "cook",
            "cooper",
            "cox",
            "craig",
            "cross",
            "curtis",
            "daly",
            "daniels",
            "davidson",
            "davis",
            "dawson",
            "day",
            "dean",
            "dempsey",
            "diaz",
            "dixon",
            "doyle",
            "duffy",
            "duncan",
            "dunn",
            "edwards",
            "elliott",
            "ellis",
            "ervin",
            "evans",
            "everett",
            "fisher",
            "fleming",
            "fletcher",
            "ford",
            "foster",
            "francis",
            "franklin",
            "freeman",
            "french",
            "fry",
            "fuller",
            "garcia",
            "gardner",
            "garner",
            "garrett",
            "george",
            "gibbs",
            "gibson",
            "gilbert",
            "gill",
            "glover",
            "gomez",
            "gonzales",
            "gonzalez",
            "goodman",
            "goodwin",
            "gordon",
            "graham",
            "grant",
            "graves",
            "gray",
            "green",
            "greene",
            "griffin",
            "groves",
            "guerrero",
            "hall",
            "hamilton",
            "hammond",
            "hampton",
            "hansen",
            "hanson",
            "hardy",
            "harmon",
            "harper",
            "harrington",
            "harris",
            "harrison",
            "hart",
            "harvey",
            "hawkins",
            "hayes",
            "henderson",
            "henry",
            "hernandez",
            "herrera",
            "hicks",
            "higgins",
            "hill",
            "hodges",
            "hoffman",
            "hogan",
            "holland",
            "hopkins",
            "horton",
            "houston",
            "howard",
            "hudson",
            "hughes",
            "hunt",
            "hunter",
            "hurst",
            "ingram",
            "jackson",
            "jacobs",
            "james",
            "jennings",
            "jensen",
            "jimenez",
            "johns",
            "johnson",
            "jones",
            "jordan",
            "joseph",
            "judd",
            "keller",
            "kelly",
            "kennedy",
            "kennedy",
            "khan",
            "king",
            "knight",
            "kramer",
            "lambert",
            "lane",
            "larson",
            "lawrence",
            "lawson",
            "lee",
            "leonard",
            "lewis",
            "lindsey",
            "little",
            "logan",
            "long",
            "lopez",
            "love",
            "lowe",
            "lucas",
            "lynch",
            "lyons",
            "mack",
            "maldonado",
            "malone",
            "mann",
            "marshall",
            "martin",
            "martinez",
            "massey",
            "mason",
            "mathis",
            "matthews",
            "maxwell",
            "mcdaniel",
            "mcdonald",
            "mckenzie",
            "mclaughlin",
            "mejia",
            "melton",
            "mendez",
            "mendoza",
            "miles",
            "miller",
            "mills",
            "miranda",
            "mitchell",
            "moody",
            "moore",
            "moran",
            "morgan",
            "morris",
            "morrison",
            "moss",
            "mullins",
            "munoz",
            "murphy",
            "murray",
            "myers",
            "navarro",
            "neal",
            "nelson",
            "newman",
            "nguyen",
            "nichols",
            "nicholson",
            "norman",
            "norris",
            "norton",
            "nunez",
            "obrian",
            "oliver",
            "olsen",
            "ortega",
            "owen",
            "owens",
            "pacheco",
            "padilla",
            "page",
            "palmer",
            "parker",
            "parsons",
            "patel",
            "patton",
            "paul",
            "payne",
            "peck",
            "pena",
            "perez",
            "perkins",
            "perry",
            "peters",
            "peterson",
            "phillips",
            "pierce",
            "pittman",
            "pope",
            "porter",
            "potter",
            "powell",
            "powers",
            "price",
            "prince",
            "quinn",
            "ramirez",
            "ramos",
            "randall",
            "reed",
            "reese",
            "reeves",
            "reid",
            "reyes",
            "reynolds");
    List<String> homeworkSynonyms =
        List.of(
            "assignment",
            "task",
            "exercise",
            "project",
            "paper",
            "report",
            "writeup",
            "worksheet",
            "case_study",
            "problem_set",
            "lab_report",
            "practice",
            "essay",
            "takehome",
            "submission",
            "review",
            "deliverable",
            "draft",
            "quiz",
            "writeup_task");

    List<String> separators =
        List.of(
            "_", // underscore
            "-", // dash
            " ", // space
            ".", // dot
            "__", // double underscore
            "--", // double dash
            "~", // tilde
            "=", // equal sign
            "+", // plus sign
            "" // no separator (just mashing words together)
            );

    List<String> suffixes =
        List.of(
            "_v", // common version prefix
            "-v", // another version prefix
            "(1)", // versioning as suffix
            "(final)", // final tag
            "(rev)", // revised tag
            "_final", // very common tag in homework files
            "_copy", // when students make a last-minute duplicate
            "_done", // "I'm done!" kind of tag
            "-last" // last version before deadline
            );
    List<Function<String, String>> casing = List.of(String::toLowerCase, String::toUpperCase);

    return IntStream.range(0, count)
        .mapToObj(
            i -> {
              String fName = fistNames.get(ThreadLocalRandom.current().nextInt(fistNames.size()));
              String lName = lastNames.get(ThreadLocalRandom.current().nextInt(lastNames.size()));
              String taskType =
                  homeworkSynonyms.get(
                      ThreadLocalRandom.current().nextInt(homeworkSynonyms.size()));
              String separator =
                  separators.get(ThreadLocalRandom.current().nextInt(separators.size()));
              String suffix = suffixes.get(ThreadLocalRandom.current().nextInt(suffixes.size()));
              Function<String, String> caseTransformer =
                  casing.get(ThreadLocalRandom.current().nextInt(casing.size()));

              var nameParts = Arrays.asList(fName, lName);
              Collections.shuffle(nameParts);
              var taskTypeNameParts =
                  Arrays.asList(
                      nameParts.stream()
                          .map(caseTransformer)
                          .collect(Collectors.joining(separator)),
                      taskType);
              Collections.shuffle(taskTypeNameParts);

              return String.join(separator, taskTypeNameParts)
                  + separator
                  + suffix
                  + separator
                  + ThreadLocalRandom.current().nextInt(10)
                  + ".docx";
            })
        .collect(Collectors.toList());
  }

  // Simulate a set of random student first names
  private static List<String> generateKeywords(int count) {
    return IntStream.range(0, count)
        .mapToObj(i -> fistNames.get(ThreadLocalRandom.current().nextInt(fistNames.size())))
        .collect(Collectors.toList());
  }

  @Test
  public void testSearch() {
    List<String> keywords = List.of("raw", "agg");
    List<String> inputs =
        List.of("raw_subjectivity_1", "raw_subjectivity_2", "agg_subjectivity", "raw_discount");
    var actual = SearchUtil.search(keywords, inputs);
    assertEquals(inputs, actual);
  }

  @Test
  public void testSearch2() {
    List<String> keywords = List.of("Alice", "Bob");
    List<String> inputs =
        List.of(
            "final_report_Alice_version3.docx",
            "homework2_Bob_final.pdf",
            "assignment_final_version.docx",
            "Bobcat_assignment.doc");
    List<String> expected =
        List.of(
            "final_report_Alice_version3.docx", "homework2_Bob_final.pdf", "Bobcat_assignment.doc");
    var actual = SearchUtil.search(keywords, inputs);

    assertEquals(expected, actual);
  }

  @Test
  public void testSearch3() {
    List<String> keywords = List.of("Charlie", "Dana");
    List<String> inputs =
        List.of(
            "homework_final_2023.docx", "finalversion_v2_uploadthisone.pdf", "dontknowwhose.doc");
    List<String> expected = List.of();

    var actual = SearchUtil.search(keywords, inputs);

    assertEquals(expected, actual);
  }

  @Test
  public void testSearch4() {
    List<String> keywords = List.of("Eve");
    List<String> inputs =
        List.of("homework_eve.docx", "EVE_final_submission.doc", "summary_Eve2023_final.pdf");
    List<String> expected = List.of("summary_Eve2023_final.pdf");

    var actual = SearchUtil.search(keywords, inputs);

    assertEquals(expected, actual);
  }

  @Test
  public void testSearch5() {
    List<String> keywords = List.of("Frank", "Grace");
    List<String> inputs =
        List.of(
            "final_Frank_Grace_submission.docx",
            "notes_for_Grace_only.pdf",
            "reviewed_by_Frank.doc",
            "extra_assignment.doc");
    List<String> expected =
        List.of(
            "final_Frank_Grace_submission.docx",
            "notes_for_Grace_only.pdf",
            "reviewed_by_Frank.doc");

    var actual = SearchUtil.search(keywords, inputs);

    assertEquals(expected, actual);
  }

  @Test
  public void testSearch6() {
    List<String> keywords = List.of();
    List<String> inputs = List.of("homework_Judy_v2.doc", "markus_essay.docx");
    List<String> expected = List.of();

    var actual = SearchUtil.search(keywords, inputs);

    assertEquals(expected, actual);
  }

  @Test
  public void testSearch7() {
    List<String> keywords = List.of("Harry");
    List<String> inputs = List.of();
    List<String> expected = List.of();

    var actual = SearchUtil.search(keywords, inputs);

    assertEquals(expected, actual);
  }

  @Test
  public void testSearch8() {
    List<String> keywords = List.of("Leo");
    List<String> inputs =
        List.of(
            "Leonardo_submission.docx",
            "Leo_v3_final.pdf",
            "geometry_homework.doc",
            "cleopatra_summary.docx");
    List<String> expected = List.of("Leonardo_submission.docx", "Leo_v3_final.pdf");

    var actual = SearchUtil.search(keywords, inputs);

    assertEquals(expected, actual);
  }

  @Test
  public void testSearch9() {
    List<String> keywords = generateKeywords(30);
    List<String> inputs = generateInputs(2000000);
    // Files.write(Path.of("resources"),inputs.stream().collect(Collectors.joining(System.lineSeparator())))

    var sw = new StopWatch();
    sw.start();
    var actual = SearchUtil.search(keywords, inputs);
    System.out.println("Size: " + actual.size());
    sw.stop();
    System.out.println("Time taken: " + sw.getTotalTimeMillis() + "ms");
    if (sw.getTotalTimeMillis() > 1000) throw new AssertionFailure("Slow");
  }

  @Test
  public void testUnmatched0() {
    List<String> keywords = List.of("Leo", "Martin", "Fanni");
    List<String> inputs =
        List.of(
            "Leonardo_submission.docx",
            "Fanni_v3_final.pdf",
            "geometry_homework.doc",
            "cleopatra_summary_Martin.docx");
    Boolean expected = false;

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    assertEquals(expected, actual);
  }

  @Test
  public void testUnmatched1() throws IOException {
    String testNo = "01";
    List<String> keywords = List.of("Leo", "Martin", "Fanni");
    List<String> inputs = List.of("Leonardo_submission.docx", "geometry_homework.doc");
    Boolean expected = true;

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  @Test
  public void testUnmatched2() throws IOException {
    String testNo = "02";
    List<String> keywords = List.of("Alice", "Bob");
    List<String> inputs = List.of("charlie_notes.txt", "homework_final.pdf");
    Boolean expected = true;

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  // 3. No keywords provided
  @Test
  public void testUnmatched3() throws IOException {
    String testNo = "03";

    List<String> keywords = List.of();
    List<String> inputs = List.of("charlie_notes.txt", "homework_final.pdf");
    Boolean expected = false;

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  // 4. No files provided
  @Test
  public void testUnmatched4() throws IOException {
    String testNo = "04";

    List<String> keywords = List.of("Alice", "Bob");
    List<String> inputs = List.of();
    Boolean expected = true;

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  // 5. Both lists empty
  @Test
  public void testUnmatched5() throws IOException {
    String testNo = "05";

    List<String> keywords = List.of();
    List<String> inputs = List.of();
    Boolean expected = false;

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  // 6. Case-sensitive mismatch
  @Test
  public void testUnmatched6() throws IOException {
    String testNo = "06";

    List<String> keywords = List.of("alice");
    List<String> inputs = List.of("Alice_homework.docx");
    Boolean expected = true; // "alice" != "Alice"

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  // 7. Keyword is a substring not matched as a word
  @Test
  public void testUnmatched7() throws IOException {
    String testNo = "07";

    List<String> keywords = List.of("Ann");
    List<String> inputs = List.of("Joanna_submission.pdf");
    Boolean expected = true;

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  // 8. Long overlapping names
  @Test
  public void testUnmatched8() throws IOException {
    String testNo = "08";

    List<String> keywords = List.of("Ann", "Anna");
    List<String> inputs = List.of("Annabelle_notes.txt", "Anna_submission.pdf");
    Boolean expected = false; // Both are substrings

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  // 9. One file matching multiple keywords
  @Test
  public void testUnmatched9() throws IOException {
    String testNo = "09";

    List<String> keywords = List.of("Alice", "Bob");
    List<String> inputs = List.of("AliceBob_combined_project.docx");
    Boolean expected = false;

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  // 10. Keyword partially in file but not fully matching
  @Test
  public void testUnmatched10() throws IOException {
    String testNo = "10";

    List<String> keywords = List.of("Rob");
    List<String> inputs = List.of("Robert_notes.txt");
    Boolean expected = false; // "Rob" is in "Robert"

    var actual = SearchUtil.unmatchedStudentExists(keywords, inputs);

    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input" + testNo + " .txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    assertEquals(expected, actual);
  }

  @Test
  public void testUnmatched11() throws IOException {
    String testNo = "11";
    Set<String> keywords = new HashSet<>(generateKeywords(100));
    Set<String> inputs = new HashSet<>(generateInputs(500000));
    // Files.write(Path.of("resources"),inputs.stream().collect(Collectors.joining(System.lineSeparator())))

    var sw = new StopWatch();
    sw.start();
    var actual =
        SearchUtil.unmatchedStudentExists(new ArrayList<>(keywords), new ArrayList<>(inputs));
    System.out.println("Exists?: " + actual);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input11.txt"),
        List.of(String.valueOf(keywords.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input11.txt"),
        keywords,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input11.txt"),
        List.of(String.valueOf(inputs.size())),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\input11.txt"),
        inputs,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);
    Files.write(
        Path.of("C:\\Users\\kvint\\Downloads\\output" + testNo + " .txt"),
        List.of(actual ? "1" : "0"),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND);

    sw.stop();
    System.out.println("Time taken: " + sw.getTotalTimeMillis() + "ms");
    if (sw.getTotalTimeMillis() > 1000) throw new AssertionFailure("Slow");
  }
}
