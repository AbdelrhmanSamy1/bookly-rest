package com.example.bookly.config;

import com.example.bookly.entity.Author;
import com.example.bookly.entity.Book;
import com.example.bookly.entity.Category;
import com.example.bookly.repository.AuthorRepository;
import com.example.bookly.repository.BookRepository;
import com.example.bookly.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            log.info("Database already seeded — skipping.");
            return;
        }

        log.info("Seeding database with dummy data...");

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        //  Categories
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        Category fantasy   = categoryRepository.save(Category.builder().name("Fantasy").description("Epic worlds, magic systems, and mythical creatures").build());
        Category sciFi     = categoryRepository.save(Category.builder().name("Science Fiction").description("Futuristic technology, space exploration, and dystopian societies").build());
        Category mystery   = categoryRepository.save(Category.builder().name("Mystery").description("Crime-solving, detective stories, and suspenseful thrillers").build());
        Category romance   = categoryRepository.save(Category.builder().name("Romance").description("Love stories, relationships, and emotional journeys").build());
        Category nonFiction = categoryRepository.save(Category.builder().name("Non-Fiction").description("Real-world knowledge, biographies, and self-improvement").build());
        Category horror    = categoryRepository.save(Category.builder().name("Horror").description("Terrifying tales, supernatural events, and psychological scares").build());

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        //  Authors
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        Author tolkien   = authorRepository.save(Author.builder().name("J.R.R. Tolkien").bio("English writer and philologist, author of The Lord of the Rings and The Hobbit.").nationality("British").build());
        Author rowling   = authorRepository.save(Author.builder().name("J.K. Rowling").bio("British author best known for the Harry Potter fantasy series.").nationality("British").build());
        Author asimov    = authorRepository.save(Author.builder().name("Isaac Asimov").bio("Russian-born American author and biochemist, a prolific writer of science fiction.").nationality("American").build());
        Author christie  = authorRepository.save(Author.builder().name("Agatha Christie").bio("English writer known for her detective novels featuring Hercule Poirot and Miss Marple.").nationality("British").build());
        Author austen    = authorRepository.save(Author.builder().name("Jane Austen").bio("English novelist known for her sharp social commentary and romantic fiction.").nationality("British").build());
        Author king      = authorRepository.save(Author.builder().name("Stephen King").bio("American author of horror, supernatural fiction, and suspense novels.").nationality("American").build());
        Author orwell    = authorRepository.save(Author.builder().name("George Orwell").bio("English novelist, essayist, and critic, famous for 1984 and Animal Farm.").nationality("British").build());
        Author dune      = authorRepository.save(Author.builder().name("Frank Herbert").bio("American science-fiction author best known for the Dune saga.").nationality("American").build());
        Author sanderson = authorRepository.save(Author.builder().name("Brandon Sanderson").bio("American fantasy and science fiction author known for the Cosmere universe.").nationality("American").build());
        Author clear     = authorRepository.save(Author.builder().name("James Clear").bio("American author and speaker focused on habits, decision-making, and continuous improvement.").nationality("American").build());

        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
        //  Books
        // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

        // Fantasy
        bookRepository.save(Book.builder()
                .title("The Lord of the Rings").isbn("9780618640157")
                .description("An epic high-fantasy novel following hobbits, elves, and men in their quest to destroy the One Ring and defeat the Dark Lord Sauron.")
                .price(new BigDecimal("24.99")).stockQuantity(50).publishedYear(1954)
                .author(tolkien).category(fantasy).build());

        bookRepository.save(Book.builder()
                .title("The Hobbit").isbn("9780547928227")
                .description("Bilbo Baggins embarks on an unexpected journey with a group of dwarves to reclaim their homeland from the dragon Smaug.")
                .price(new BigDecimal("14.99")).stockQuantity(75).publishedYear(1937)
                .author(tolkien).category(fantasy).build());

        bookRepository.save(Book.builder()
                .title("Harry Potter and the Sorcerer's Stone").isbn("9780590353427")
                .description("An orphaned boy discovers he is a wizard and begins his education at Hogwarts School of Witchcraft and Wizardry.")
                .price(new BigDecimal("12.99")).stockQuantity(120).publishedYear(1997)
                .author(rowling).category(fantasy).build());

        bookRepository.save(Book.builder()
                .title("Harry Potter and the Chamber of Secrets").isbn("9780439064873")
                .description("Harry returns to Hogwarts for his second year and uncovers the mystery of the Chamber of Secrets.")
                .price(new BigDecimal("13.99")).stockQuantity(95).publishedYear(1998)
                .author(rowling).category(fantasy).build());

        bookRepository.save(Book.builder()
                .title("The Way of Kings").isbn("9780765326355")
                .description("The first book in the Stormlight Archive — an epic fantasy saga set on the world of Roshar, ravaged by magical storms.")
                .price(new BigDecimal("18.99")).stockQuantity(60).publishedYear(2010)
                .author(sanderson).category(fantasy).build());

        bookRepository.save(Book.builder()
                .title("Mistborn: The Final Empire").isbn("9780765311788")
                .description("In a world of ash and mist, a young street thief discovers she has powers that could topple the immortal ruler known as the Lord Ruler.")
                .price(new BigDecimal("15.99")).stockQuantity(45).publishedYear(2006)
                .author(sanderson).category(fantasy).build());

        // Science Fiction
        bookRepository.save(Book.builder()
                .title("Foundation").isbn("9780553293357")
                .description("A mathematician devises a plan to preserve knowledge as the Galactic Empire collapses around him.")
                .price(new BigDecimal("16.99")).stockQuantity(40).publishedYear(1951)
                .author(asimov).category(sciFi).build());

        bookRepository.save(Book.builder()
                .title("I, Robot").isbn("9780553294385")
                .description("A collection of interconnected stories exploring the three laws of robotics and their consequences.")
                .price(new BigDecimal("13.99")).stockQuantity(55).publishedYear(1950)
                .author(asimov).category(sciFi).build());

        bookRepository.save(Book.builder()
                .title("Dune").isbn("9780441013593")
                .description("On the desert planet Arrakis, young Paul Atreides navigates politics, religion, and ecology in this masterpiece of science fiction.")
                .price(new BigDecimal("19.99")).stockQuantity(65).publishedYear(1965)
                .author(dune).category(sciFi).build());

        bookRepository.save(Book.builder()
                .title("1984").isbn("9780451524935")
                .description("In a totalitarian society ruled by Big Brother, one man dares to think forbidden thoughts and dream of rebellion.")
                .price(new BigDecimal("11.99")).stockQuantity(100).publishedYear(1949)
                .author(orwell).category(sciFi).build());

        // Mystery
        bookRepository.save(Book.builder()
                .title("Murder on the Orient Express").isbn("9780062693662")
                .description("Detective Hercule Poirot must solve a murder aboard the luxurious Orient Express, where every passenger is a suspect.")
                .price(new BigDecimal("12.99")).stockQuantity(70).publishedYear(1934)
                .author(christie).category(mystery).build());

        bookRepository.save(Book.builder()
                .title("And Then There Were None").isbn("9780062073488")
                .description("Ten strangers are lured to an isolated island and begin to die one by one according to a chilling nursery rhyme.")
                .price(new BigDecimal("11.99")).stockQuantity(80).publishedYear(1939)
                .author(christie).category(mystery).build());

        bookRepository.save(Book.builder()
                .title("The ABC Murders").isbn("9780062073587")
                .description("Poirot receives letters from a killer who signs them 'ABC' — each murder following alphabetical order.")
                .price(new BigDecimal("10.99")).stockQuantity(45).publishedYear(1936)
                .author(christie).category(mystery).build());

        // Romance
        bookRepository.save(Book.builder()
                .title("Pride and Prejudice").isbn("9780141439518")
                .description("Elizabeth Bennet navigates issues of manners, morality, and marriage in Regency-era England, clashing with the proud Mr. Darcy.")
                .price(new BigDecimal("9.99")).stockQuantity(90).publishedYear(1813)
                .author(austen).category(romance).build());

        bookRepository.save(Book.builder()
                .title("Sense and Sensibility").isbn("9780141439662")
                .description("Two sisters — one guided by sense, the other by sensibility — face love and heartbreak in 19th-century England.")
                .price(new BigDecimal("8.99")).stockQuantity(60).publishedYear(1811)
                .author(austen).category(romance).build());

        // Horror
        bookRepository.save(Book.builder()
                .title("The Shining").isbn("9780307743657")
                .description("Jack Torrance takes a winter caretaker job at the isolated Overlook Hotel, where supernatural forces drive him toward madness.")
                .price(new BigDecimal("14.99")).stockQuantity(55).publishedYear(1977)
                .author(king).category(horror).build());

        bookRepository.save(Book.builder()
                .title("It").isbn("9781501142970")
                .description("In the town of Derry, Maine, seven children confront an ancient evil that takes the form of a terrifying clown called Pennywise.")
                .price(new BigDecimal("17.99")).stockQuantity(40).publishedYear(1986)
                .author(king).category(horror).build());

        bookRepository.save(Book.builder()
                .title("Pet Sematary").isbn("9781501156700")
                .description("A family discovers a mysterious burial ground behind their new home that has the power to raise the dead — but at a terrible cost.")
                .price(new BigDecimal("13.99")).stockQuantity(35).publishedYear(1983)
                .author(king).category(horror).build());

        // Non-Fiction
        bookRepository.save(Book.builder()
                .title("Atomic Habits").isbn("9780735211292")
                .description("A proven framework for building good habits and breaking bad ones — tiny changes, remarkable results.")
                .price(new BigDecimal("16.99")).stockQuantity(150).publishedYear(2018)
                .author(clear).category(nonFiction).build());

        bookRepository.save(Book.builder()
                .title("Animal Farm").isbn("9780451526342")
                .description("A satirical allegory of the Russian Revolution, told through the story of farm animals who overthrow their human farmer.")
                .price(new BigDecimal("8.99")).stockQuantity(110).publishedYear(1945)
                .author(orwell).category(nonFiction).build());

        log.info("Database seeded with {} authors, {} categories, {} books.",
                authorRepository.count(), categoryRepository.count(), bookRepository.count());
    }
}
