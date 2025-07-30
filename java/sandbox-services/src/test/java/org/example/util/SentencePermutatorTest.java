package org.example.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class SentencePermutatorTest {

    String str = """
                Lorem ipsum dolor sit amet
                Nullam iaculis vitae est e
                sed eros sit amet convalli
                auctor ornare ex. Donec li
                accumsan ultrices sapien. 
                In vestibulum imperdiet ni
                Ut quis ex felis. Nulla fa
                Vivamus nulla nibh, laoree
                Duis nec ultricies leo. Mo
                Etiam tempor lacinia leo a
                Aliquam erat volutpat. Don
                Suspendisse vitae aliquet 
                semper commodo sem. Vivamu
                Morbi ac risus interdum, v
                Curabitur malesuada tellus
                Fusce cursus ipsum nec arc
                quis malesuada lacus facil
                Donec in elit non purus co
                Suspendisse potenti. Etiam
                in porttitor efficitur, du
                vitae viverra nibh urna at
                rhoncus ante in, accumsan 
                ut convallis id, eleifend 
                non dignissim eros viverra
                Nullam vitae gravida sem, 
                rhoncus turpis ut, suscipi
                Nulla facilisi. Donec in e
                Vestibulum hendrerit nibh 
                Phasellus tempor lectus qu
                Proin pellentesque, lacus 
                purus risus rhoncus mauris
                Quisque sagittis diam non 
                vitae ultricies eros effic
                pellentesque quis nunc. Ae
                Praesent eu ultricies enim
                condimentum massa nec, viv
                ut accumsan neque. Pellent
                Sed sagittis dapibus neque
                Vivamus lacinia iaculis se
                porttitor congue. Aenean e
                Sed aliquet elit vitae urn
                Sed a pellentesque quam. N
                Ut pretium, erat ut varius
                a pharetra enim nunc sit a
                Lorem ipsum dolor sit amet
                Nullam iaculis vitae est e
                sed eros sit amet convalli
                auctor ornare ex. Donec li
                accumsan ultrices sapien. 
                In vestibulum imperdiet ni
                Ut quis ex felis. Nulla fa
                Vivamus nulla nibh, laoree
                Duis nec ultricies leo. Mo
                Etiam tempor lacinia leo a
                Aliquam erat volutpat. Don
                Suspendisse vitae aliquet 
                semper commodo sem. Vivamu
                Morbi ac risus interdum, v
                Curabitur malesuada tellus
                Fusce cursus ipsum nec arc
                quis malesuada lacus facil
                Donec in elit non purus co
                Suspendisse potenti. Etiam
                in porttitor efficitur, du
                vitae viverra nibh urna at
                rhoncus ante in, accumsan 
                ut convallis id, eleifend 
                non dignissim eros viverra
                Nullam vitae gravida sem, 
                rhoncus turpis ut, suscipi
                Nulla facilisi. Donec in e
                Vestibulum hendrerit nibh 
                Phasellus tempor lectus qu
                Proin pellentesque, lacus 
                purus risus rhoncus mauris
                Quisque sagittis diam non 
                vitae ultricies eros effic
                pellentesque quis nunc. Ae
                Praesent eu ultricies enim
                condimentum massa nec, viv
                ut accumsan neque. Pellent
                Sed sagittis dapibus neque
                Vivamus lacinia iaculis se
                porttitor congue. Aenean e
                Sed aliquet elit vitae urn
                Sed a pellentesque quam. N
                Ut pretium, erat ut varius
                a pharetra enim nunc sit a
                Lorem ipsum dolor sit amet
                Nullam iaculis vitae est e
                sed eros sit amet convalli
                auctor ornare ex. Donec li
                accumsan ultrices sapien. 
                In vestibulum imperdiet ni
                Ut quis ex felis. Nulla fa
                Vivamus nulla nibh, laoree
                Duis nec ultricies leo. Mo
                Etiam tempor lacinia leo a
                Aliquam erat volutpat. Don
                Suspendisse vitae aliquet 
                semper commodo sem. Vivamu
                Morbi ac risus interdum, v
                Curabitur malesuada tellus
                Fusce cursus ipsum nec arc
                quis malesuada lacus facil
                Donec in elit non purus co
                Suspendisse potenti. Etiam
                in porttitor efficitur, du
                vitae viverra nibh urna at
                rhoncus ante in, accumsan 
                ut convallis id, eleifend 
                non dignissim eros viverra
                Nullam vitae gravida sem, 
                rhoncus turpis ut, suscipi
                Nulla facilisi. Donec in e
                Vestibulum hendrerit nibh 
                Phasellus tempor lectus qu
                Proin pellentesque, lacus 
                purus risus rhoncus mauris
                Quisque sagittis diam non 
                vitae ultricies eros effic
                pellentesque quis nunc. Ae
                Praesent eu ultricies enim
                condimentum massa nec, viv
                ut accumsan neque. Pellent
                Sed sagittis dapibus neque
                Vivamus lacinia iaculis se
                porttitor congue. Aenean e
                Sed aliquet elit vitae urn
                Sed a pellentesque quam. N
                Ut pretium, erat ut varius
                a pharetra enim nunc sit a
                Lorem ipsum dolor sit amet
                Nullam iaculis vitae est e
                sed eros sit amet convalli
                auctor ornare ex. Donec li
                accumsan ultrices sapien. 
                In vestibulum imperdiet ni
                Ut quis ex felis. Nulla fa
                Vivamus nulla nibh, laoree
                Duis nec ultricies leo. Mo
                Etiam tempor lacinia leo a
                Aliquam erat volutpat. Don
                Suspendisse vitae aliquet 
                semper commodo sem. Vivamu
                Morbi ac risus interdum, v
                Curabitur malesuada tellus
                Fusce cursus ipsum nec arc
                quis malesuada lacus facil
                Donec in elit non purus co
                Suspendisse potenti. Etiam
                in porttitor efficitur, du
                vitae viverra nibh urna at
                rhoncus ante in, accumsan 
                ut convallis id, eleifend 
                non dignissim eros viverra
                Nullam vitae gravida sem, 
                rhoncus turpis ut, suscipi
                Nulla facilisi. Donec in e
                Vestibulum hendrerit nibh 
                Phasellus tempor lectus qu
                Proin pellentesque, lacus 
                purus risus rhoncus mauris
                Quisque sagittis diam non 
                vitae ultricies eros effic
                pellentesque quis nunc. Ae
                Praesent eu ultricies enim
                condimentum massa nec, viv
                ut accumsan neque. Pellent
                Sed sagittis dapibus neque
                Vivamus lacinia iaculis se
                porttitor congue. Aenean e
                Sed aliquet elit vitae urn
                Sed a pellentesque quam. N
                Ut pretium, erat ut varius
                a pharetra enim nunc sit a
                Lorem ipsum dolor sit amet
                Nullam iaculis vitae est e
                sed eros sit amet convalli
                auctor ornare ex. Donec li
                accumsan ultrices sapien. 
                In vestibulum imperdiet ni
                Ut quis ex felis. Nulla fa
                Vivamus nulla nibh, laoree
                Duis nec ultricies leo. Mo
                Etiam tempor lacinia leo a
                Aliquam erat volutpat. Don
                Suspendisse vitae aliquet 
                semper commodo sem. Vivamu
                Morbi ac risus interdum, v
                Curabitur malesuada tellus
                Fusce cursus ipsum nec arc
                quis malesuada lacus facil
                Donec in elit non purus co
                Suspendisse potenti. Etiam
                in porttitor efficitur, du
                vitae viverra nibh urna at
                rhoncus ante in, accumsan 
                ut convallis id, eleifend 
                non dignissim eros viverra
                Nullam vitae gravida sem, 
                rhoncus turpis ut, suscipi
                Nulla facilisi. Donec in e
                Vestibulum hendrerit nibh 
                Phasellus tempor lectus qu
                Proin pellentesque, lacus 
                purus risus rhoncus mauris
                Quisque sagittis diam non 
                vitae ultricies eros effic
                pellentesque quis nunc. Ae
                Praesent eu ultricies enim
                condimentum massa nec, viv
                ut accumsan neque. Pellent
                Sed sagittis dapibus neque
                Vivamus lacinia iaculis se
                porttitor congue. Aenean e
                Sed aliquet elit vitae urn
                Sed a pellentesque quam. N
                Ut pretium, erat ut varius
                a pharetra enim nunc sit a
                Lorem ipsum dolor sit amet
                Nullam iaculis vitae est e
                sed eros sit amet convalli
                auctor ornare ex. Donec li
                accumsan ultrices sapien. 
                In vestibulum imperdiet ni
                Ut quis ex felis. Nulla fa
                Vivamus nulla nibh, laoree
                Duis nec ultricies leo. Mo
                Etiam tempor lacinia leo a
                Aliquam erat volutpat. Don
                Suspendisse vitae aliquet 
                semper commodo sem. Vivamu
                Morbi ac risus interdum, v
                Curabitur malesuada tellus
                Fusce cursus ipsum nec arc
                quis malesuada lacus facil
                Donec in elit non purus co
                Suspendisse potenti. Etiam
                in porttitor efficitur, du
                vitae viverra nibh urna at
                rhoncus ante in, accumsan 
                ut convallis id, eleifend 
                non dignissim eros viverra
                Nullam vitae gravida sem, 
                rhoncus turpis ut, suscipi
                Nulla facilisi. Donec in e
                Vestibulum hendrerit nibh 
                Phasellus tempor lectus qu
                Proin pellentesque, lacus 
                purus risus rhoncus mauris
                Quisque sagittis diam non 
                vitae ultricies eros effic
                pellentesque quis nunc. Ae
                Praesent eu ultricies enim
                condimentum massa nec, viv
                ut accumsan neque. Pellent
                Sed sagittis dapibus neque
                Vivamus lacinia iaculis se
                porttitor congue. Aenean e
                Sed aliquet elit vitae urn
                Sed a pellentesque quam. N
                Ut pretium, erat ut varius
                a pharetra enim nunc sit a
                Lorem ipsum dolor sit amet
                Nullam iaculis vitae est e
                sed eros sit amet convalli
                auctor ornare ex. Donec li
                accumsan ultrices sapien. 
                In vestibulum imperdiet ni
                Ut quis ex felis. Nulla fa
                Vivamus nulla nibh, laoree
                Duis nec ultricies leo. Mo
                Etiam tempor lacinia leo a
                Aliquam erat volutpat. Don
                Suspendisse vitae aliquet 
                semper commodo sem. Vivamu
                Morbi ac risus interdum, v
                Curabitur malesuada tellus
                Fusce cursus ipsum nec arc
                quis malesuada lacus facil
                Donec in elit non purus co
                Suspendisse potenti. Etiam
                in porttitor efficitur, du
                vitae viverra nibh urna at
                rhoncus ante in, accumsan 
                ut convallis id, eleifend 
                non dignissim eros viverra
                Nullam vitae gravida sem, 
                rhoncus turpis ut, suscipi
                Nulla facilisi. Donec in e
                Vestibulum hendrerit nibh 
                Phasellus tempor lectus qu
                Proin pellentesque, lacus 
                purus risus rhoncus mauris
                Quisque sagittis diam non 
                vitae ultricies eros effic
                pellentesque quis nunc. Ae
                Praesent eu ultricies enim
                condimentum massa nec, viv
                ut accumsan neque. Pellent
                Sed sagittis dapibus neque
                Vivamus lacinia iaculis se
                porttitor congue. Aenean e
                Sed aliquet elit vitae urn
                Sed a pellentesque quam. N
                Ut pretium, erat ut varius
                a pharetra enim nunc sit a
                Lorem ipsum dolor sit amet
                Nullam iaculis vitae est e
                sed eros sit amet convalli
                auctor ornare ex. Donec li
                accumsan ultrices sapien. 
                In vestibulum imperdiet ni
                Ut quis ex felis. Nulla fa
                Vivamus nulla nibh, laoree
                Duis nec ultricies leo. Mo
                Etiam tempor lacinia leo a
                Aliquam erat volutpat. Don
                Suspendisse vitae aliquet 
                semper commodo sem. Vivamu
                Morbi ac risus interdum, v
                Curabitur malesuada tellus
                Fusce cursus ipsum nec arc
                quis malesuada lacus facil
                Donec in elit non purus co
                Suspendisse potenti. Etiam
                in porttitor efficitur, du
                vitae viverra nibh urna at
                rhoncus ante in, accumsan 
                ut convallis id, eleifend 
                non dignissim eros viverra
                Nullam vitae gravida sem, 
                rhoncus turpis ut, suscipi
                Nulla facilisi. Donec in e
                Vestibulum hendrerit nibh 
                Phasellus tempor lectus qu
                Proin pellentesque, lacus 
                purus risus rhoncus mauris
                Quisque sagittis diam non 
                vitae ultricies eros effic
                pellentesque quis nunc. Ae
                Praesent eu ultricies enim
                condimentum massa nec, viv
                ut accumsan neque. Pellent
                Sed sagittis dapibus neque
                Vivamus lacinia iaculis se
                porttitor congue. Aenean e
                Sed aliquet elit vitae urn
                Sed a pellentesque quam. N
                Ut pretium, erat ut varius
                a pharetra enim nunc sit a
                """;


    @Test
    public void permutateWordsTest() {
        var sw = new StopWatch();
        sw.start();
        var permutatorService = new SentencePermutator();

        var lines = str.lines().map(permutatorService::permuteWords).flatMap(Collection::stream).toList();
        var words = lines.stream().map(line -> line.split(" ")).flatMap(Arrays::stream).toList();

        var email = "";
        for (String word : words){
            email = email + "\n" + word;
        }

        System.out.println(email.lines().count());

        sw.stop();
        System.out.printf("Elapsed: %.2f sec",sw.getTotalTimeSeconds());;
    }
    @Test
    public void permutateWordsTest2() {
        var sw = new StopWatch();
        sw.start();
        var permutatorService = new SentencePermutator();

        var lines = str.lines().map(permutatorService::permuteWords).flatMap(Collection::stream).toList();
        var words = lines.stream().map(line -> line.split(" ")).flatMap(Arrays::stream).toList();

        var email = new StringBuilder();
        for (String word : words){
            email.append("\n").append(word);
        }

        System.out.println(email.toString().lines().count());
        sw.stop();
        System.out.printf("Elapsed: %.2f sec",sw.getTotalTimeSeconds());
    }

}