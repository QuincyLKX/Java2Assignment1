package assignment1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This is just a demo for you, please run it on JDK17. This is just a demo, and you can extend and
 * implement functions based on this demo, or implement it in a different way.
 */
public class OnlineCoursesAnalyzer {

  List<Course> courses = new ArrayList<>();

  public OnlineCoursesAnalyzer(String datasetPath) {
    BufferedReader br = null;
    String line;
    try {
        br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
            Course course = new Course(info[0], info[1], new Date(info[2]), info[3], info[4],
                info[5],
                Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]),
                Integer.parseInt(info[9]), Integer.parseInt(info[10]),
                Double.parseDouble(info[11]),
                Double.parseDouble(info[12]), Double.parseDouble(info[13]),
                Double.parseDouble(info[14]),
                Double.parseDouble(info[15]), Double.parseDouble(info[16]),
                Double.parseDouble(info[17]),
                Double.parseDouble(info[18]), Double.parseDouble(info[19]),
                Double.parseDouble(info[20]),
                Double.parseDouble(info[21]), Double.parseDouble(info[22]));
            courses.add(course);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

//1
public Map<String, Integer> getPtcpCountByInst() {
    Map<String, Integer> getPtcpCountByInst = courses.stream()
        .collect(
            Collectors.groupingBy(Course::getInstitution,
                Collectors.summingInt(Course::getParticipants)));
    List<Map.Entry<String, Integer>> list = new ArrayList<>(getPtcpCountByInst.entrySet());
    list.sort((o1, o2) -> {
        for (int i = 0; i < Math.min(o1.getKey().length(), o2.getKey().length()); i++) {
            int c1;
            int c2;
            if (o1.getKey().charAt(i) >= 97 && o1.getKey().charAt(i) <= 122) {
                c1 = o1.getKey().charAt(i) - 32;
            } else {
                c1 = o1.getKey().charAt(i);
            }
            if (o2.getKey().charAt(i) >= 97 && o2.getKey().charAt(i) <= 122) {
                c2 = o2.getKey().charAt(i) - 32;
            } else {
                c2 = o2.getKey().charAt(i);
            }
            if (c1 > c2) {
                return 1;
            } else if (c1 < c2) {
                return -1;
            }
        }
        return Integer.compare(o2.getKey().length(), o1.getKey().length());
    });
    return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
}

//2
public Map<String, Integer> getPtcpCountByInstAndSubject() {
    Map<String, Integer> getPtcpCountByInstAndSubject = courses.stream()
        .collect(Collectors.groupingBy(Course::getInsAndSub,
            Collectors.summingInt(Course::getParticipants)));

    List<Map.Entry<String, Integer>> list = new ArrayList<>(
        getPtcpCountByInstAndSubject.entrySet());

    list.sort((o1, o2) -> {
        if (!o1.getValue().equals(o2.getValue())) {
            return o2.getValue().compareTo(o1.getValue());
        } else {
            for (int i = 0; i < Math.min(o1.getKey().length(), o2.getKey().length()); i++) {
                int c1;
                int c2;
                if (o1.getKey().charAt(i) >= 97 && o1.getKey().charAt(i) <= 122) {
                    c1 = o1.getKey().charAt(i) - 32;
                } else {
                    c1 = o1.getKey().charAt(i);
                }
                if (o2.getKey().charAt(i) >= 97 && o2.getKey().charAt(i) <= 122) {
                    c2 = o2.getKey().charAt(i) - 32;
                } else {
                    c2 = o2.getKey().charAt(i);
                }
                if (c1 > c2) {
                    return 1;
                } else if (c1 < c2) {
                    return -1;
                }
            }
            return Integer.compare(o2.getKey().length(), o1.getKey().length());
        }


    });
    Map<String, Integer> result = new LinkedHashMap<>();
    list.forEach(s -> result.put(s.getKey(), s.getValue()));
    return result;
}

//3
public Map<String, List<List<String>>> getCourseListOfInstructor() {
    ArrayList<String> instructors = new ArrayList<>();
    courses.forEach(s -> {
        String[] temp = s.instructors.split(", ");
        for (String value : temp) {
            if (!instructors.contains(value)) {
                instructors.add(value);
            }
        }
    });
    instructors.replaceAll(String::trim);
    List<List<String>> list0 = new ArrayList<>();
    List<List<String>> list1 = new ArrayList<>();
    for (String ins : instructors) {
        List<String> in = new ArrayList<>();
        List<String> co = new ArrayList<>();
        courses.stream()
            .filter(s -> {
                String[] result = s.instructors.split(", ");
                for (String value : result) {
                    if (value.equals(ins)) {
                        return true;
                    }
                }
                return false;
            })
            .forEach(s -> {
                if (s.instructors.equals(ins)) {
                    in.add(s.title);
                } else {
                    co.add(s.title);
                }
            });
        list0.add(in.stream()
            .distinct()
            .sorted((s1, s2) -> {
                for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
                    int c1 = s1.charAt(i);
                    int c2 = s2.charAt(i);
                    if (c1 > c2) {
                        return 1;
                    } else if (c1 < c2) {
                        return -1;
                    }
                }
                return Integer.compare(s1.length(), s2.length());
            }).toList());
        list1.add(co.stream()
            .distinct()
            .sorted((s1, s2) -> {
                for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
                    int c1 = s1.charAt(i);
                    int c2 = s2.charAt(i);
                    if (c1 > c2) {
                        return 1;
                    } else if (c1 < c2) {
                        return -1;
                    }
                }
                return Integer.compare(s1.length(), s2.length());
            }).toList());
    }
    Map<String, List<List<String>>> getCourseListOfInstructor = new LinkedHashMap<>();
    for (int i = 0; i < instructors.size(); i++) {
        List<List<String>> list = new ArrayList<>();
        list.add(list0.get(i));
        list.add(list1.get(i));
        getCourseListOfInstructor.put(instructors.get(i), list);
    }
    return getCourseListOfInstructor;
}

//4
public List<String> getCourses(int topK, String by) {
    if (by.equals("hours")) {
        List<String> getCourses = new ArrayList<>();
        courses.stream().sorted(Comparator.comparing(Course::getTotalHours).reversed())
            .filter(distinctByKey(s -> s.title)).limit(topK)
            .forEach(s -> getCourses.add(s.title));
        return getCourses;
    } else if (by.equals("participants")) {
        List<String> getCourses = new ArrayList<>();
        courses.stream().sorted(Comparator.comparing(Course::getParticipants).reversed())
            .filter(distinctByKey(s -> s.title)).distinct().limit(topK)
            .forEach(s -> getCourses.add(s.title));
        return getCourses;
    } else {
        return null;
    }
}

//5
public List<String> searchCourses(String courseSubject, double percentAudited,
    double totalCourseHours) {
    List<String> result = new ArrayList<>();
    courses.stream().filter(
            s -> (s.percentAudited >= percentAudited) && (s.totalHours <= totalCourseHours)
                && (s.subject.toUpperCase().contains(courseSubject.toUpperCase())))
        .forEach(s -> result.add(s.title));
    Collections.sort(result);
    return result.stream().distinct().toList();
}

//6
public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
    Map<String, Double> averageMedianAge = courses.stream()
        .collect(Collectors.groupingBy(Course::getNumber,
            Collectors.averagingDouble(Course::getMedianAge)));
    Map<String, Double> averageMale = courses.stream()
        .collect(Collectors.groupingBy(Course::getNumber,
            Collectors.averagingDouble(Course::getPercentMale)));
    Map<String, Double> averageDegree = courses.stream()
        .collect(Collectors.groupingBy(Course::getNumber,
            Collectors.averagingDouble(Course::getPercentDegree)));
    List<String> numbers = new ArrayList<>(
        courses.stream().map(s -> s.number).distinct().toList());
    Map<String, Double> numberAndValue = new HashMap<>();
    for (String temp : numbers) {
        double value =
            (age - averageMedianAge.get(temp)) * (age - averageMedianAge.get(temp)) + (
                gender * 100 - averageMale.get(temp)) * (
                gender * 100 - averageMale.get(temp)) + (
                isBachelorOrHigher * 100 - averageDegree.get(temp)) * (
                isBachelorOrHigher * 100 - averageDegree.get(temp));
        numberAndValue.put(temp, value);
    }
    Map<String,String> numberAndTitle = new HashMap<>();
    for (String num : numbers) {
        courses.stream()
            .filter(s -> s.number.equals(num))
            .sorted((s1, s2) -> s2.launchDate.compareTo(s1.launchDate))
            .limit(1)
            .forEach(s -> numberAndTitle.put(num,s.title));
    }
    numbers.sort((s1,s2) -> {
        double value1 = numberAndValue.get(s1);
        double value2 = numberAndValue.get(s2);
        if (value1 > value2) return 1;
        else if (value1 < value2) return -1;
        else {
            String str1 = numberAndTitle.get(s1);
            String str2 = numberAndTitle.get(s2);
            for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
                int c1 = str1.charAt(i);
                int c2 = str2.charAt(i);
                if (c1 > c2) {
                    return 1;
                } else if (c1 < c2) {
                    return -1;
                }
            }
            return Integer.compare(str1.length(), str2.length());
        }
    });
    List<String> result = new ArrayList<>();
    for (String number : numbers) {
        if (!result.contains(numberAndTitle.get(number))) {
    result.add(numberAndTitle.get(number));
        if (result.size() == 10) {
          break;
        }
      }
    }
    return result;
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

class Course {

  String institution;
  String number;
  Date launchDate;
  String title;
  String instructors;
  String subject;
  int year;
  int honorCode;
  int participants;
  int audited;
  int certified;
  double percentAudited;
  double percentCertified;
  double percentCertified50;
  double percentVideo;
  double percentForum;
  double gradeHigherZero;
  double totalHours;
  double medianHoursCertification;
  double medianAge;
  double percentMale;
  double percentFemale;
  double percentDegree;

  public double getMedianAge() {
        return medianAge;
    }

  public double getPercentMale() {
        return percentMale;
    }

  public double getPercentDegree() {
        return percentDegree;
    }

  public String getNumber() {
        return number;
    }

  public double getTotalHours() {
        return totalHours;
    }

  public String getInstitution() {
        return institution;
    }

  public int getParticipants() {
        return participants;
    }

  public String getSubject() {
        return subject;
    }

  public String getInsAndSub() {
        return String.format("%s-%s", getInstitution(), getSubject());
    }

  public Course(String institution, String number, Date launchDate,
    String title, String instructors, String subject,
    int year, int honorCode, int participants,
    int audited, int certified, double percentAudited,
    double percentCertified, double percentCertified50,
    double percentVideo, double percentForum, double gradeHigherZero,
    double totalHours, double medianHoursCertification,
    double medianAge, double percentMale, double percentFemale,
    double percentDegree) {
    this.institution = institution;
    this.number = number;
    this.launchDate = launchDate;
    if (title.startsWith("\"")) {
        title = title.substring(1);
    }
    if (title.endsWith("\"")) {
        title = title.substring(0, title.length() - 1);
    }
    this.title = title;
    if (instructors.startsWith("\"")) {
        instructors = instructors.substring(1);
    }
    if (instructors.endsWith("\"")) {
        instructors = instructors.substring(0, instructors.length() - 1);
    }
    this.instructors = instructors;
    if (subject.startsWith("\"")) {
        subject = subject.substring(1);
    }
    if (subject.endsWith("\"")) {
        subject = subject.substring(0, subject.length() - 1);
    }
    this.subject = subject;
    this.year = year;
    this.honorCode = honorCode;
    this.participants = participants;
    this.audited = audited;
    this.certified = certified;
    this.percentAudited = percentAudited;
    this.percentCertified = percentCertified;
    this.percentCertified50 = percentCertified50;
    this.percentVideo = percentVideo;
    this.percentForum = percentForum;
    this.gradeHigherZero = gradeHigherZero;
    this.totalHours = totalHours;
    this.medianHoursCertification = medianHoursCertification;
    this.medianAge = medianAge;
    this.percentMale = percentMale;
    this.percentFemale = percentFemale;
    this.percentDegree = percentDegree;
  }
}